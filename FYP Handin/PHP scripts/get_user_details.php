<?php
 
/*
 * Following code will get single users details
 */
 
// array for JSON response
$response = array();

 
// Connecting to mysql database
	$con = mysqli_connect("192.168.0.44:3308", "root1", "toor", "users");
 
// check for post data
if (isset($_GET["id"])) {
    $id = $_GET['id'];
 
    // get a product from products table
	$query = "SELECT *FROM products WHERE id = $id";
    $result = mysqli_query($con, $query);
 
    if (!empty($result)) {
        // check for empty result
        if (mysqli_num_rows($result) > 0) {
 
            $result = mysqli_fetch_array($result);
 
            $user = array();
            $user["id"] = $result["id"];
            $user["forename"] = $result["forename"];
            $user["surname"] = $result["surname"];
            $user["bandstatus"] = $result["bandstatus"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["user"] = array();
 
            array_push($response["user"], $user);
 
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