<?php
 
/*
 * Following code will update a product information
 * A user is identified by id
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['id'])) {
 
    $id = $_POST['id'];
 
	// Connecting to mysql database
	$con = mysqli_connect("192.168.0.44:3308", "root1", "toor", "users");
	
	// mysql update row with matched id
	$query = "UPDATE products SET bandstatus = '0' WHERE id = $id";
    $result = mysqli_query($con, $query);


    // check if set
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "band set";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>