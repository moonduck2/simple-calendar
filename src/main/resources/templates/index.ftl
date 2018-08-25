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
			<label for="newRoomName">회의실 등록</label> 
			<input type="text" class="form-control" id="newRoomName" placeholder="Enter room name"> 
		</div>
		<button type="submit" class="btn btn-primary">Submit</button>
	</form>
	
	<form id="register_meeting">
		<div class="form-group">
			<label for="newMeetingTitle">회의명</label> 
			<input type="text" class="form-control" id="newMeetingTitle" placeholder="Enter meeting title">
			
			<label for="newMeetingContent">회의 요약</label>
			<textarea class="form-control" rows="5" id="newMeetingContent"></textarea>
			
			<label for="newMeetingOwner">주최자</label>
			<input class="form-control" id="newMeetingOwner">

			<label for="newMeetingStartDatePicker">시작일</label>
			<div class='input-group date' id='newMeetingStartDatePicker'>
				<input type='text' class="form-control" id="newMeetingStartDate"/> 
				<span	class="input-group-addon"> 
					<span class="glyphicon glyphicon-calendar"></span>
				</span>
			</div>
			
			<label for="newMeetingEndDatePicker">종료일</label>
			<div class='input-group date' id='newMeetingEndDatePicker'>
				<input type='text' class="form-control" id="newMeetingEndDate"/> 
				<span	class="input-group-addon"> 
					<span class="glyphicon glyphicon-calendar"></span>
				</span>
			</div>
			
			<label for="newMeetingStartTimePicker">시작시간</label>
			<div class='input-group date' id='newMeetingStartTimePicker'>
				<input type='text' class="form-control" id="newMeetingStartTime"/> 
				<span	class="input-group-addon"> 
					<span class="glyphicon glyphicon-calendar"></span>
				</span>
			</div>
			
			<label for="newMeetingEndTimePicker">종료시간</label>
			<div class='input-group date' id='newMeetingEndTimePicker'>
				<input type='text' class="form-control" id="newMeetingEndTime"/> 
				<span	class="input-group-addon"> 
					<span class="glyphicon glyphicon-calendar"></span>
				</span>
			</div>
			
			<label for="newMeetingAllRooms">회의실</label>
			<div class="form-group" id="newMeetingAllRooms">
			  <select class="form-control" id="roomSelectBox">
			  </select>
			</div>
			
			<label for="newMeetingRecurrence">반복설정</label>
			<div class="form-group" id="newMeetingRecurrence">
				<label class="checkbox-inline"><input type="checkbox" value="" id="isRecurrence">반복</label>
			  <label><input type="radio" name="recurrenceDayOfWeek" data-order=1 disabled>월</label>
			  <label><input type="radio" name="recurrenceDayOfWeek" data-order=2 disabled>화</label>
			  <label><input type="radio" name="recurrenceDayOfWeek" data-order=3 disabled>수</label>
			  <label><input type="radio" name="recurrenceDayOfWeek" data-order=4 disabled>목</label>
			  <label><input type="radio" name="recurrenceDayOfWeek" data-order=5 disabled>금</label>
			  <label><input type="radio" name="recurrenceDayOfWeek" data-order=6 disabled>토</label>
			  <label><input type="radio" name="recurrenceDayOfWeek" data-order=7 disabled>일</label>
			  <label><input type="number" id=recurrenceNumber>반복횟수</label>
			</div>
			
		</div>
		<button type="submit" class="btn btn-primary">Submit</button>
	</form>

	<script id="roomList" type="text/x-handlebars-template">
		{{#each @root}}
			<option data-room-id={{id}}>{{name}}</option>
		{{/each}}
	</script>
	<script>
		$(document).ready(function() {
			var meetingRoomListTemplate = Handlebars.compile($("#roomList").html());
			
			function setAllMeetingRoom(roomList) {
				$("#roomSelectBox").html(meetingRoomListTemplate(roomList))
			}
			function init() {
				var today = new Date(), todayStr = today.getFullYear()
					+ "-" + ("0" + (today.getMonth() + 1)).slice(-2)
					+ "-" + ("0" + today.getDate()).slice(-2);
				
				$.ajax({
					url : "http://localhost:8080/api/meeting?date=" + todayStr,
					crossDomain : true,
					success : function(data) {
						console.log(data)
						setAllMeetingRoom(data)
					}
				})	
			}
			function clearNewMeeting() {
				$("#newMeetingStartDate").val('');
				$("#newMeetingEndDate").val('');
				$("#newMeetingTitle").val('');
				$("#newMeetingContent").val('');
				$("#newMeetingOwner").val('');
				$("#newMeetingStartTime").val('');
				$("#newMeetingEndTime").val('');
			}
			$("#register_meeting").submit(function(e) {
				e.preventDefault();
				var data = {
						startDate : $("#newMeetingStartDate").val(),
						endDate : $("#newMeetingEndDate").val(),
						title : $("#newMeetingTitle").val(),
						content : $("#newMeetingContent").val(),
						userName : $("#newMeetingOwner").val(),
						startTime : $("#newMeetingStartTime").val(),
						endTime : $("#newMeetingEndTime").val(),
						meetingRoom: {
							id : $("#roomSelectBox option:selected").data('room-id')
						},
				};
				if($("#isRecurrence").is(":checked")) {
					data.recurrence = {
							dayOfWeek : $("#newMeetingRecurrence input:radio:checked").data("order"),
							type : "ONCE_A_WEEK",
							count : parseInt($("#recurrenceNumber").val())
					}
				}
				$.ajax({
					url : "http://localhost:8080/api/meeting",
					method : "post",
					contentType : "application/json;charset=utf-8",
					dataType : 'json',
					data : JSON.stringify(data),
					success : function(data) {
						clearNewMeeting()
						//init();
					}
				})
				return false;
			})
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
			$('#isRecurrence').change(function() {
				var $radio = $("#newMeetingRecurrence input:radio") 
        if($(this).is(":checked")) {
        	$radio.attr('disabled', false)
        } else {
        	$radio.attr('checked', false)
        	$radio.attr('disabled', true)
        }
    });
			init();
		})
	</script>
</body>
</html>