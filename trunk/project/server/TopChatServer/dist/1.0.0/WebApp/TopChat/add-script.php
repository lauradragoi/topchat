<?php
	$username = $_POST['username'];
	$password = $_POST['password'];
	$password2 = $_POST['password2'];	
	
	if ($password != $password2)
		die("Mismatching passwords <a href=\"index.php\">Go back</a>");
		
	if (!defined("DB"))
	{
		define ("DB", "topchatserver");
		define ("USER", "root");
		define ("PASS", "");
	}
	
	$link = mysql_connect("localhost", USER, PASS);			
	$db = new mysqli('localhost', USER, PASS, DB);
	
	$query = "SELECT * FROM `users` WHERE username='" . $username . "'";
	$result = $db->query($query);			
	
	if ($result)
	{
		$row = $result->fetch_assoc();
		if ($row)
			die("User already exists! <a href=\"index.php\">Go back</a>");
	}
	
	$query = "INSERT INTO `users` (username, password) VALUES ('" . $username . "' , '" . $password . "')";				
	$result = $db->query($query);
	
	echo "User successfully added! <a href=\"index.php\">Go back</a>";
?>