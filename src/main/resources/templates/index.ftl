<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Meeting room</title>
	
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
	<script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js"></script>

</head>
<body>
	<form id="register_room">
		<div class="form-group">
			<label for="">회의실 등록</label> 
			<input type="text" class="form-control" id="newRoomName" placeholder="Enter room name"> 
		</div>
		<button type="submit" class="btn btn-primary">Submit</button>
	</form>

	<script>
		$(document).ready(function() {
			function init() {
				var today = new Date(), todayStr = today.getFullYear()
					+ "-" + ("0" + (today.getMonth() + 1)).slice(-2)
					+ "-" + ("0" + today.getDate()).slice(-2);
	
				$.ajax({
					url : "http://localhost:8080/api/meeting?date=" + todayStr,
					crossDomain : true,
					success : function(data) {
						console.log(data)
					}
				})	
			}
			
			$("#register_room").submit(function(e) {
				var roomName = $("#newRoomName").val();
				if (!roomName || roomName.trim() === '') {
					alert("회의실 이름을 정확히 기입해주세요!");
					return false;
				}
				$.ajax({
					url : "http://localhost:8080/api/room",
					method : "post",
					contentType : "application/json;charset=utf-8",
					dataType : 'json',
					data : JSON.stringify({
						name : roomName
					}),
					success : function(data) {
						$("#newRoomName").val('')
						init();
					}
				})
				return false;
			})
			
			
		})
	</script>
</body>
</html>