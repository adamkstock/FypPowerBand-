<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();

 
// check for required fields
if (isset($_POST['name']) && isset($_POST['price']) && isset($_POST['description'])) {
 
    $name = $_POST['name'];
    $price = $_POST['price'];
    $description = $_POST['description'];
 

	// Connecting to mysql database
	$con = mysqli_connect("192.168.0.44:3308", "root1", "toor", "androidhive");

    // mysql inserting a new row
	$query = "INSERT INTO products(name, price, description) VALUES('$name', '$price', '$description')";
	
    $result = mysqli_query($con, $query);
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>