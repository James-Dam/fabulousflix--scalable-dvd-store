package EmployeeDashboard;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

import org.jasypt.util.password.StrongPasswordEncryptor;

@WebServlet(name = "MetadataServlet", urlPatterns = "/api/metadata")
public class MetadataServlet extends HttpServlet {
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject responseJsonObject = new JsonObject();
        JsonObject tablesJson = new JsonObject(); // Store tables as keys with column arrays

        try (Connection conn = dataSource.getConnection()) {

            String getMetadata = "SELECT table_name, column_name, data_type " +
                    "FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE table_schema = 'moviedb';";

            PreparedStatement prepareData = conn.prepareStatement(getMetadata);
            ResultSet executeQuery = prepareData.executeQuery();

            responseJsonObject.addProperty("status", "ok");

            while (executeQuery.next()) {
                String tableName = executeQuery.getString("TABLE_NAME");

                // If this is the first column for the table, create an array
                if (!tablesJson.has(tableName)) {
                    tablesJson.add(tableName, new JsonArray());
                }

                // Create JSON object for each column
                JsonObject columnData = new JsonObject();
                columnData.addProperty("column_name", executeQuery.getString("COLUMN_NAME"));
                columnData.addProperty("data_type", executeQuery.getString("DATA_TYPE"));

                // Add column data to the respective table array
                tablesJson.getAsJsonArray(tableName).add(columnData);
            }

            responseJsonObject.add("tables", tablesJson);

        } catch (Exception e) {
            // Retrieval fail
            responseJsonObject.addProperty("status", e.getMessage());
            // Log to localhost log
            request.getServletContext().log("Couldn't retrieve metadata", e);
        }
        response.getWriter().write(responseJsonObject.toString());
    }
}
