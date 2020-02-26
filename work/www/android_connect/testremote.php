<?php
	echo "1";
	include 'db_config.php';
	$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
	$query = "INSERT INTO products(name, price, description) VALUES('test', '1', 'test')";
	echo "Connected Successfully";

    // mysql inserting a new row
    if($result = mysqli_query($con, $query))
	{
		echo "Returned values are : ".mysqli_num_rows($result);
		mysqli_free_result($result);
	}
	echo "fin";
	
	
	
	
	
?>