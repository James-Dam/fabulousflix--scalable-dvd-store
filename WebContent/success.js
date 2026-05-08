function handleResult(resultData) {
    console.log("handleResult: populating shopping cart data");

    // Find the empty table body by id cart_table_body
    let movieTableBodyElement = jQuery("#cart_table_body");
    let total = 0;

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < Math.min(100, resultData.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";

        rowHTML +=
            "<th>" +
            resultData[i]["sale_id"] +
            "</th>";

        rowHTML +=
            "<th>" +
            '<a href="./movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_title"] +
            '</a>' +
            "</th>"

        rowHTML +=
            "<th>" +
            resultData[i]["quantity"] +
            "</th>";

        rowHTML +=
            "<th>" +
            resultData[i]["price"] +
            "</th>";

        rowHTML +=
            "<th>" +
            resultData[i]["total"] +
            "</th>";

        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
        total += resultData[i]["total"]
    }

    jQuery("#total_amount").text(`Total: $${total}`);
}


function goBackToMovies() {
    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: "api/get-movie-session",
        success: (sessionData) => {
            let params = new URLSearchParams(sessionData);
            window.location.href = "movies-list.html?" + params.toString();
        },
        error: (err) => {
            console.error("Error retrieving session data:", err);
            window.location.href = "movie-list.html";
        }
    });
}

jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET", // Setting request method
    url: "api/payment-confirmation",
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
