
<li>Project 1: https://www.youtube.com/watch?v=vZTTsqC6jso&t=19s&ab_channel=ColinHarrison </li>
<li>Project 2: https://www.youtube.com/watch?v=e4ULRRYqZZ4&t=2s</li>
<li>Project 3: https://www.youtube.com/watch?v=qYTMTcwylLU&ab_channel=ColinHarrison</li>
<li>Project 4: https://youtu.be/ftSkSJjwR7s</li>
<li>Project 5: https://youtu.be/ftSkSJjwR7s</li>

<h2>Project 2 substring implementation</h2>
<p>Used %search% format for all string searches, finding all entries containing string search substring</p>
<p>Used browse_letter% format for all browsing by letter, finding all entries starting with browse letter</p>

<h2>Project 3 Prepared Statement files</h2>
<p>InsertMovieServlet.java</p>
<p>InsertStarServlet.java</p>
<p>AddToCartServlet.java</p>
<p>EmployeeLoginServlet.java</p>
<p>LoginServlet.java</p>
<p>MovieListServlet.java</p>
<p>MovieServlet.java</p>
<p>PaymentConfirmationServlet.java</p>
<p>PaymentServlet.java</p>
<p>StarServlet.java</p>

<h2>Project 4 Connection Pooling </h2>
<p>Configuration path: WebContent/META-INF/context.xml</p>
<p>Connection pooling used in all servlets with prepared statements, files stated above</p>
<p>Instead of creating a new connection for every query, we use a set of pre-established connections, our connection pool. Each servlet grabs a connection from the pool when they need it, and return it back to the pool when done instead of closing it</p>
<p>With two backend sql instances, we create two seperate connection pools, as defined in our context.xml, one for each backend db. When servlets grab a connection they grab from either of the connection pools depending on their needs</p>

<h2>Project 4 Master-Slave </h2>
<p>Configuration path: WebContent/META-INF/context.xml</p>
<p>Code for determing route is in all servlets with prepared statements, files stated above</p>
<p>All queries that write to the database in any way are routed to the master instance. If the query is just reading from db, the query is randomly routed to either the slave or master</p>

<h2>Project 5 Endpoints</h2>
<p>Served by fabflix-login: /api/login, /api/employee_login</p>
<p>Served by fabflix-movie: everything else</p>
