# FabFlix - Java/MySQL DVD E-Commerce Platform

FabFlix is a full-stack DVD e-commerce web application built for a UC Irvine database systems course. The project emphasizes Java servlet backend development, relational database access, search and browsing workflows, shopping cart sessions, employee dashboard tooling, and scalable deployment concepts using MySQL replication, Docker, and Kubernetes.

## Tech Stack

- **Backend:** Java 11, Jakarta Servlets, Apache Tomcat, JDBC, Gson
- **Database:** MySQL, stored procedures, JNDI-configured Tomcat connection pools
- **Frontend:** HTML, CSS, JavaScript, jQuery AJAX
- **Auth/Security:** Session-based access control, JWT cookie utility, reCAPTCHA integration, prepared statements, input validation
- **Deployment:** Maven WAR packaging, Docker, Kubernetes manifests, Tomcat JNDI resources

## Architecture / Full-Stack Request Flow

```text
Browser / JavaScript Frontend
        |
        v  AJAX / HTTP
Java Servlets on Apache Tomcat
        |
        v  JDBC + PreparedStatements + Connection Pool
MySQL Master/Slave Databases
        |
        v
JSON responses rendered by JavaScript
```

Most pages are static HTML/CSS/JavaScript under `WebContent/`. The JavaScript layer sends AJAX requests to servlet endpoints such as `/api/movie-list`, `/api/login`, `/api/shopping-cart`, and `/api/insert_movie`. Servlets query MySQL through JNDI data sources defined in `WebContent/META-INF/context.xml`, then return JSON for the frontend to render.

## Technical Highlights

- Database-backed movie search, browsing, filtering, sorting, and pagination
- Movie detail and star detail pages with related genres, stars, and ratings
- Customer login, employee login, session-gated routes, and reCAPTCHA integration
- Shopping cart and checkout flow backed by server-side session state and sales inserts
- Employee dashboard for metadata viewing, star insertion, and movie insertion through a stored procedure
- SQL injection mitigation through prepared statements and server-side validation for selected query parameters
- JDBC connection pooling to reduce database connection overhead
- Master/slave MySQL configuration used for read-heavy endpoints and write routing to the master database
- Docker and Kubernetes configuration files for course deployment experiments

## Key Backend Flows

### Movie Search / Filtering / Pagination

`MovieListServlet` handles `/api/movie-list` requests from the movie list, search, and browse pages. It builds a SQL query from optional filters such as title, year, director, star name, genre, and browse letter. Title search uses MySQL full-text search in boolean mode, while pagination applies `LIMIT` and `OFFSET` so large result sets are not returned at once. The servlet returns movie records with top genres and stars as JSON.

### Authentication / Login

`LoginServlet` and `EmployeeLoginServlet` receive login form submissions over POST. They validate credentials against the `customers` or `employees` tables using prepared statements, then store the authenticated user in the HTTP session. The employee login flow also verifies reCAPTCHA before checking credentials.

### Shopping Cart / Checkout

Cart endpoints store selected movie IDs and quantities in the server-side HTTP session. `ShoppingCartServlet` enriches cart entries with movie title and price data from MySQL. `PaymentServlet` validates payment details against the customer and credit card tables, writes sales rows to the master database, and stores generated sale IDs in the session. `PaymentConfirmationServlet` reads those sale IDs to render the confirmation page and clears checkout session state afterward.

### Employee Dashboard / Movie Insertion

The employee dashboard exposes backend-only workflows for viewing schema metadata, inserting stars, and adding movies. `InsertMovieServlet` calls the `add_movie` stored procedure in `stored-procedure.sql`, which centralizes movie/star/genre insertion logic and reports whether related records already existed or were created.

### Frontend JSON Flow

Frontend scripts in `WebContent/*.js` and `WebContent/employee/*.js` use jQuery AJAX to call servlet endpoints. Servlets respond with JSON arrays or objects, and the browser updates tables, forms, cart totals, and confirmation views without server-rendered pages.

## Project Structure

```text
src/
  common/                  Login filter and JWT cookie utilities
  login/                   Customer and employee authentication servlets
  movie/                   Movie, search, cart, checkout, autocomplete servlets
  movie/EmployeeDashboard/ Employee metadata and insertion servlets
  movie/XMLParser/         XML parsing utilities for course data loading
WebContent/
  *.html, *.css, *.js      Frontend pages and AJAX clients
  META-INF/context.xml     Tomcat JNDI datasource configuration
docs/                      Project notes, endpoint docs, interview notes
diagrams/                  Existing architecture diagrams
Dockerfile                 Tomcat container build
fabflix*.yaml              Kubernetes deployment manifests
stored-procedure.sql       Movie insertion stored procedure
pom.xml                    Maven WAR build configuration
```

## My Contributions

Based on the project contribution notes, James Dam contributed across the stack:

- Set up JDBC/MySQL connectivity and database-backed servlet logic
- Worked on movie list, movie detail, and star detail backend/frontend pages
- Implemented login, payment queries, pagination, filtering, and session persistence for movie list state
- Added HTTPS-related course configuration, reCAPTCHA, and employee dashboard features
- Created stored procedure support for movie insertion
- Implemented full-text movie title search and autocomplete behavior
- Helped containerize the app and adapt it to a multi-service Kubernetes architecture

See [docs/contributions.md](docs/contributions.md) for the full team contribution breakdown.

## Engineering Tradeoffs

- **Server-side sessions for cart state:** Simple and effective for a course app, but production systems may prefer durable carts tied to users and explicit session expiration behavior.
- **Pagination:** Keeps movie list responses manageable and avoids returning huge result sets, at the cost of more request parameters and state management.
- **Prepared statements:** Reduce SQL injection risk for values, while dynamic SQL fragments such as sort fields still require server-side whitelisting.
- **Connection pooling:** Reduces repeated database connection overhead, but requires Tomcat/JNDI configuration to match each deployment environment.
- **Read replicas:** Useful for read-heavy movie endpoints, though replica lag and deterministic routing would need more careful handling in production.
- **Course deployment configs:** Docker and Kubernetes manifests demonstrate deployment concepts, but cloud hostnames, credentials, and database resources should be reviewed before reuse.

## Running Locally / Course Environment Notes

This repository was built for a UC Irvine course environment. Some database, cloud, and Tomcat settings are course-specific and may require modification before the app runs locally.

Likely requirements:

- Java 11 and Maven
- Apache Tomcat compatible with the servlet/Jakarta dependencies used by the project
- MySQL with the FabFlix `moviedb` schema and course data loaded
- Tomcat JNDI datasource entries for `jdbc/masterdb` and `jdbc/slavedb`
- `stored-procedure.sql` installed if using the employee movie insertion flow
- Optional Docker/Kubernetes setup if testing the course deployment configuration

Build artifact:

```bash
mvn package
```

The Maven build produces a WAR named `cs122b_project1_war`, matching several frontend paths used in the project.

## Documentation Links

- [API Endpoints](docs/api-endpoints.md)
- [Interview Notes](docs/interview-notes.md)
- [Contributions](docs/contributions.md)
- [Course Milestones](docs/course-milestones.md)
- [Scalability and Deployment](docs/scalability-and-deployment.md)
- [Security and Database Notes](docs/security-and-database-notes.md)
- [XML Parser Report](docs/xml-parser-report.md)
