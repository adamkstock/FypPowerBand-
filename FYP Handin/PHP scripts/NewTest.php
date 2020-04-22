<?php
	echo "1";
	$con = mysqli_connect("192.168.0.44:3308", "root1", "toor", "fyppowerband002");
	$query = "INSERT INTO users(forename, surname, bandstatus) VALUES('test', 'surtest', '2')";
	$result = mysqli_query($con, $query);
	echo "2";

?>