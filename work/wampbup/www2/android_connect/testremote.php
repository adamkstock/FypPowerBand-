<?php
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO products(name, price, description) VALUES('test', '1', 'test')");
 
	
	
	
	
	
?>