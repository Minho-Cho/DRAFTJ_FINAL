<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="us">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>회의보기</title>
<link href="jqueryui/jquery-ui.css" rel="stylesheet">
<style>

body {
	font-family: "Trebuchet MS", sans-serif;
}

.demoHeaders {
	margin-top: 2em;
}

#dialog-link {
	padding: .4em 1em .4em 20px;
	text-decoration: none;
	position: relative;
}

#dialog-link span.ui-icon {
	margin: 0 5px 0 0;
	position: absolute;
	left: .2em;
	top: 50%;
	margin-top: -8px;
}

#icons {
	margin: 0;
	padding: 0;
}

#icons li {
	margin: 2px;
	position: relative;
	padding: 4px 0;
	cursor: pointer;
	float: left;
	list-style: none;
}

#icons span.ui-icon {
	float: left;
	margin: 0 4px;
}

.fakewindowcontain .ui-widget-overlay {
	position: absolute;
}

select {
	width: 200px;
}

table.info {
	width: 100%;
	border: 1px solid #8C8C8C;
	border-collapse: collapse;
}

th.info, td.info, th.info {
	border: 1px solid #8C8C8C;
	padding: 5px;
	margin: 1px;
}

th.info {
	background: #f6f6f6;
	text-align: center;
}
</style>
</head>
<script src="jquery/js/jquery-1.7.2.js"></script>
<script src="jqueryui/jquery-ui.js"></script>
<script>
	
	$(document).ready(function() {
		var date = "";	
		var location ="";
		var attendee = "";
		var content = "";

		date = "<b>2018-07-10 10:30</b>"; 
	     $('#date').html(date);
	
		 location = "<b>702호</b>"; 
	     $('#location').html(location);
	     
	     attendee = "<b>홍길동</b>"; 
	     $('#attendee').html(attendee);
	     
	     $.ajax({
				type : 'GET',
				dataType : 'json',
				url : '/findMeetingLog',
				complete : function(result) {
// 					console.log(result.responseText);
					content = result.responseText;
					$('#content').html(content);
   				 	$("#content").append("<br><br><h2 style='margin-bottom: 0px;'>Keywords</h2>");
				 	$("#content").append("<br><p style='margin-top: 0px;'>DRAFT J, 부문, 아이디어 공모전</p>");
				},
				success : function() {
				},
				error : function(e) {
				}
			});
	});
	
     
</script>
<body>
	<div class="ui-corner-all ui-widget ui-widget-content tabcontent"
		style="width: 100%; height: 750px; float: left;">
		<table style="width: 90%; margin: auto;">
			<tr>
				<td align="center"><h1>사전 회의</h1></td>
			</tr>
			<tr>
				<td>
					<div>
						<table class="info" style="width: 100%; margin: auto;">
							<tr class="info">
								<th class="info" style="width: 15%;">일시</th>
								<td id="date" class="info" style="width: 35%;"></td>
								<th class="info" style="width: 15%;">장소</th>
								<td id="location" class="info" style="width: 35%;"></td>
							</tr>
							<tr class="info">
								<th class="info" style="width: 15%;">참석자</th>
								<td id="attendee" class="info" style="width: 35%; font-size: 15px;" colspan="3"></td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div>
						<table class="info"
							style="width: 100%; height: 520px; margin: auto;">
							<tr class="info">
								<td class="info" style="width: 100%;">
									<div id="divStt" style="height: 460px; overflow: auto;">
										<p id="content"></p>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
