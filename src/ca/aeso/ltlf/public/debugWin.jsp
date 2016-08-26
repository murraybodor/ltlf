<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>LTLF Debug Info</title>
		<script type="text/javascript">
		var MESSAGE_LENGTH_LIMIT = 2048;
		function appendMessage(message)
		{
			var textArea = document.getElementById("ltlfDebug");
			if (!textArea)
				return;
				
			textArea.value += message;
			var len = textArea.value.length;
			if (len > MESSAGE_LENGTH_LIMIT)
				textArea.value = textArea.value.substring(len - MESSAGE_LENGTH_LIMIT);
			textArea.scrollTop = textArea.scrollHeight;
		}
		function clearMessage()
		{
			document.getElementById("ltlfDebug").value = "";
		}
		</script>
	</head>
	<body>
		<table width="100%" height="100%">
			<tr>
				<td align="center" valign="middle">
					<table>
						<tr>
							<td>
								<textarea style="width:560px;height:360px" id="ltlfDebug"><%= request.getParameter("msg") == null ? "" : request.getParameter("msg") %></textarea>
							</td>
						</tr>
						<tr>
							<td align="center">
								<input type="button" value="Clear" onclick="clearMessage()">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>