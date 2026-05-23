# Security and Database Notes

## Prepared Statements and Input Validation

Prepared statements are used across many database-backed servlet files to reduce SQL injection risk for user-supplied values.

Relevant files include:

- `src/movie/EmployeeDashboard/InsertMovieServlet.java`
- `src/movie/EmployeeDashboard/InsertStarServlet.java`
- `src/movie/AddToCartServlet.java`
- `src/login/EmployeeLoginServlet.java`
- `src/login/LoginServlet.java`
- `src/movie/MovieListServlet.java`
- `src/movie/MovieServlet.java`
- `src/movie/PaymentConfirmationServlet.java`
- `src/movie/PaymentServlet.java`
- `src/movie/StarServlet.java`

Dynamic SQL fragments such as sort columns and sort directions require separate server-side whitelisting because they cannot be safely bound as prepared-statement values.

## Authentication and Access Control

- Customer login checks the `customers` table and stores the authenticated user in the HTTP session.
- Employee login checks the `employees` table, verifies reCAPTCHA, and uses Jasypt password checking for employee passwords.
- `LoginFilter` protects most routes by requiring a session user, while allowing public login and static resources.
- The code includes a JWT cookie utility used during customer login, alongside the servlet session model.

## Security Claims to Keep Precise

- Prefer: "SQL injection mitigation through prepared statements and input validation."
- Avoid: "complete SQL injection prevention."
- Prefer: "reCAPTCHA integration for employee login."
- Avoid: implying every login path is production-hardened, because customer reCAPTCHA verification is currently commented out in `LoginServlet`.
- Prefer: "course deployment configuration includes HTTPS-related setup."
- Avoid: claiming the repository is production-grade without further hardening.

## Database and Scalability Notes

- `WebContent/META-INF/context.xml` defines Tomcat JNDI connection pools for `jdbc/masterdb` and `jdbc/slavedb`.
- Write-oriented servlets such as cart price updates, payment, star insertion, and movie insertion use the master database.
- Several read-oriented servlets choose between master and slave data sources during servlet initialization.
- `stored-procedure.sql` contains the `add_movie` stored procedure used by the employee dashboard movie insertion flow.

## Production Hardening Ideas

- Store credentials and secrets outside committed configuration files.
- Use consistent password hashing for all user types.
- Add broader input validation and safer error responses.
- Add tests for authentication, search, checkout, and dashboard insertion flows.
- Make read/write routing deterministic and observable.
