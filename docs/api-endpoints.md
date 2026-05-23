# API Endpoints

This project exposes Java servlet endpoints that are called by the JavaScript frontend with AJAX. Paths are shown without the deployed WAR context prefix; in the course build, the full URL may include `/cs122b_project1_war`.

## Authentication

| Method | Endpoint | Description | Frontend / Backend Purpose |
| --- | --- | --- | --- |
| POST | `/api/login` | Authenticates a customer with email and password. | Used by `WebContent/login.js`; checks the `customers` table, stores the user in the session, and updates a JWT cookie utility. |
| POST | `/api/employee_login` | Authenticates an employee account. | Used by `WebContent/employee_login.js`; verifies reCAPTCHA, checks the `employees` table, and stores the employee in the session. |

## Movies / Search / Autocomplete

| Method | Endpoint | Description | Frontend / Backend Purpose |
| --- | --- | --- | --- |
| GET | `/api/movie-list` | Returns paginated movie results with optional search, browse, genre, sorting, and filtering parameters. | Used by movie list/search/browse JavaScript; queries movies, ratings, genres, and stars, then returns JSON for table rendering. |
| GET | `/api/movie` | Returns details for one movie by movie ID. | Used by `WebContent/movie.js`; loads movie metadata plus related stars and genres. |
| GET | `/api/single-star` | Returns details for one star by star ID. | Used by `WebContent/single-star.js`; loads star metadata and related movies. |
| GET | `/api/genre` | Returns available genres. | Used by browse UI code; supports genre-based browsing. |
| GET | `/api/get-movie-session` | Returns saved movie-list request parameters from the session. | Used by movie list navigation to restore filters, page, result count, and sort selections. |
| GET | `/autocomplete` | Returns title autocomplete suggestions. | Used by search UI autocomplete; queries movie titles and returns suggestion JSON. |

## Cart / Checkout

| Method | Endpoint | Description | Frontend / Backend Purpose |
| --- | --- | --- | --- |
| POST | `/api/add-to-cart` | Adds one movie to the session cart. | Used by movie list/detail JavaScript; updates server-side cart state and assigns a movie price if needed. |
| GET | `/api/shopping-cart` | Returns cart contents with title, price, quantity, and totals. | Used by `WebContent/shopping-cart.js`; enriches session cart entries with database movie data. |
| POST | `/api/alter-cart` | Increments or decrements a movie quantity in the session cart. | Used by cart controls; adjusts quantity without leaving the cart page. |
| POST | `/api/remove-from-cart` | Removes one movie from the session cart. | Used by cart controls; deletes a cart line item from session state. |
| POST | `/api/payment` | Validates payment details and records sales. | Used by checkout form JavaScript; verifies customer/credit-card data and inserts sale rows into MySQL. |
| GET | `/api/payment-confirmation` | Returns sale confirmation details. | Used by success page JavaScript; reads sale IDs from the session, returns purchased movie details, then clears checkout state. |

## Employee Dashboard

| Method | Endpoint | Description | Frontend / Backend Purpose |
| --- | --- | --- | --- |
| GET | `/api/metadata` | Returns database table and column metadata. | Used by `WebContent/employee/show_metadata.js`; reads `INFORMATION_SCHEMA.COLUMNS` for the `moviedb` schema. |
| POST | `/api/insert_star` | Inserts a new star. | Used by `WebContent/employee/insert_stars.js`; generates the next `nm...` star ID and writes to the `stars` table. |
| POST | `/api/insert_movie` | Inserts a movie and related star/genre information. | Used by `WebContent/employee/add_movie.js`; calls the `add_movie` stored procedure and returns created/existing record statuses. |
