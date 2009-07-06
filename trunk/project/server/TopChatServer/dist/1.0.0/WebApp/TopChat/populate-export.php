<?php
	if (!defined("DB"))
	{
		define ("DB", "topchatserver");
		define ("USER", "root");
		define ("PASS", "");
	}
	
	$link = mysql_connect("localhost", USER, PASS);			
	$db = new mysqli('localhost', USER, PASS, DB);
	
	$query = "SELECT DISTINCT room FROM `simplexml`";
	$result = $db->query($query);			
	
	if ($result)
	{
		while($row = $result->fetch_assoc())
		{
			echo "<option name=\"" . $row['room'] . "\">". $row['room'] ."</option>";
		}
	}
?>