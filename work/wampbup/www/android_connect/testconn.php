<?php
	include 'db_connect.php';
	$con = new DB_CONNECT();
	echo "Connected Successfully";
	mysqli_close($con);
?>
