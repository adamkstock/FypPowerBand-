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
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched id
    $result = mysql_query("UPDATE products SET bandstatus = '1' WHERE id = $id");
 
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