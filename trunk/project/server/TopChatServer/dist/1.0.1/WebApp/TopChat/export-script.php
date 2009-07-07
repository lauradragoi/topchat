<?php
	$room = $_POST['room'];
	
	if (!defined("DB"))
	{
		define ("DB", "topchatserver");
		define ("USER", "root");
		define ("PASS", "");
	}
	
	$link = mysql_connect("localhost", USER, PASS);			
	$db = new mysqli('localhost', USER, PASS, DB);
	
	$query = "SELECT * FROM `simplexml` WHERE room='" . $room . "'";
	$result = $db->query($query);			
	
	if ($result)
	{		
		header("Content-type: text/xml"); 
		echo "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		echo "<corpus>";
		echo "<Dialog team=\"" . $room ."\">";
		echo "<Participants/>";
		echo "<Topics/>";
		echo "<Body>";
     
		while($row = $result->fetch_assoc())
		{
			$ref = $row['reference'];
			if ($ref < 0)
				$ref = 0;
			echo "<Turn nickname=\"" . $row['nick']. "\">";
			echo "<Utterance genid=\"".$row['msg_id']."\" time=\"".$row['timestamp']."\" ref=\"".$ref."\">".$row['message']."</Utterance>";
			echo "</Turn>";
		}
		
		echo "</Body>";
		echo "</Dialog>";
		echo "</corpus>";
	}
				  
?>