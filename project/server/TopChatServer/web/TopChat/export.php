<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="style/main.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Export log</title>
</head>

<body>
<form method="post" action="export-script.php">
<table border="0" class="main_layout">
  <tr>   	
    <td><img src="img/logo.jpg" width="100" height="100" alt="logo" /></td>
    <td class="title">Export log</td>
  </tr>
  <tr>   	
    <td>Select room:</td>
    <td><select name="room">
    <?php
		include('populate-export.php');
    ?>
     </select></td>
  </tr> 
  <tr>
  	<td colspan="2"><input type="submit" value="Export" /></td>
  </tr>
</table>
</form>
</body>
</html>