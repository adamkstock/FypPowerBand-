<?php
	include 'db_connect';
	$con = DB_CONNECT::connect();
	echo "Connected Successfully";
	close($con);
?>
