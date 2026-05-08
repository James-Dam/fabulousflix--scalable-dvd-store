package login;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import common.JwtUtil;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jasypt.util.password.StrongPasswordEncryptor;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    // Create a dataSource which registered in web.
    private DataSource dataSource;
    private Random random = new Random();

    public void init(ServletConfig config) {
        try {
            if (random.nextBoolean()) {
                dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/masterdb");
            } else {
                dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/slavedb");
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println("LoginServlet: login request for user " + email);

        try {
            //RecaptchaVerifyUtils.verify(gRecaptchaResponse); // Verify reCAPTCHA
        } catch (Exception e) {
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "reCAPTCHA verification failed");
            response.getWriter().write(responseJsonObject.toString());
            return;
        }


        /* This example only allows username/password to be tested/test
        /  in the real project, you should talk to the database to verify username/password
        */
        JsonObject responseJsonObject = new JsonObject();

        try (Connection conn = dataSource.getConnection()) {

            // Declare our statement
            String checkEmail =
                    "SELECT * " +
                            "FROM customers c " +
                            "WHERE c.email = ? " +
                            "LIMIT 1";

            PreparedStatement getEmail = conn.prepareStatement(checkEmail);
            getEmail.setString(1, email);
            ResultSet correctEmail = getEmail.executeQuery();

            if (!correctEmail.next()) {
                throw new Exception("user " + email + " doesn't exist");
            }

            //String encryptedPassword = correctEmail.getString("password");
            String storedPassword = correctEmail.getString("password");
            boolean success = password.equals(storedPassword);
            //boolean success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);

            if (!success) {
                throw new Exception("Incorrect password");
            }

            // Login success:
            // set this user into the session
            String subject = correctEmail.getString("email");

            Map<String, Object> claims = new HashMap<>();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            claims.put("loginTime", dateFormat.format(new Date()));
            String token = JwtUtil.generateToken(subject, claims);
            JwtUtil.updateJwtCookie(request, response, token);

            System.out.println("LoginServlet: login successful for user " + email);
            request.getSession().setAttribute("user", new User(email));

            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

    } catch (Exception e) {
            // Login fail
            responseJsonObject.addProperty("status", "fail");
            // Log to localhost log
            request.getServletContext().log("Login failed");
            // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
            if (e.getMessage().contains("doesn't exist")) {
                responseJsonObject.addProperty("message", "Email " + email + " is incorrect");
            } else if (e.getMessage().contains("password")) {
                responseJsonObject.addProperty("message", "Incorrect password");
            } else {
                responseJsonObject.addProperty("message", "An error occurred. Please try again.");
            }
        }
        response.getWriter().write(responseJsonObject.toString());
    }
}