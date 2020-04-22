<?php
 
/*
 * Following code will delete a user from table
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['id'])) {
    $pid = $_POST['id'];
 
	// Connecting to mysql database
	$con = mysqli_connect("192.168.0.44:3308", "root1", "toor", "users");
 
    // mysql update row with matched pid
	$query = "DELETE FROM products WHERE id = $id";
    $result = mysqli_query($con, $query);
 
    // check if row deleted or not
    if (mysqli_affected_rows() > 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "user successfully deleted";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // no user found
        $response["success"] = 0;
        $response["message"] = "No user found";
 
        // echo no users JSON
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