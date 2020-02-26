<?php
	echo "1";
	$con = mysqli_connect("192.168.0.44:3308", "root1", "toor", "androidhive");
	$query = "INSERT INTO products(name, price, description) VALUES('test', '1', 'test')";
	$result = mysqli_query($con, $query)
?>