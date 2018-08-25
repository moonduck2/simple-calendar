<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Meeting room</title>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script type="text/javascript"
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js"></script>

</head>
<body>
	<form id="register_room">
		<div class="form-group">
			<label for="newRoomName">회의실 등록</label> <input type="text"
				class="form-control" id="newRoomName" placeholder="Enter room name">
		</div>
		<button type="submit" class="btn btn-primary">Submit</button>
	</form>

	<form id="register_meeting">
		<div class="form-group">
			<label for="newMeetingTitle">회의명</label> <input type="text"
				class="form-control" id="newMeetingTitle"
				placeholder="Enter meeting title"> <label
				for="newMeetingContent">회의 요약</label>
			<textarea class="form-control" rows="5" id="newMeetingContent"></textarea>

			<label for="newMeetingOwner">주최자</label> <input class="form-control"
				id="newMeetingOwner"> <label for="newMeetingStartDatePicker">시작일</label>
			<div class='input-group date' id='newMeetingStartDatePicker'>
				<input type='text' class="form-control" id="newMeetingStartDate" />
				<span class="input-group-addon"> <span
					class="glyphicon glyphicon-calendar"></span>
				</span>
			</div>

			<label for="newMeetingEndDatePicker">종료일</label>
			<div class='input-group date' id='newMeetingEndDatePicker'>
				<input type='text' class="form-control" id="newMeetingEndDate" /> <span
					class="input-group-addon"> <span
					class="glyphicon glyphicon-calendar"></span>
				</span>
			</div>

			<label for="newMeetingStartTimePicker">시작시간</label>
			<div class='input-group date' id='newMeetingStartTimePicker'>
				<input type='text' class="form-control" id="newMeetingStartTime" />
				<span class="input-group-addon"> <span
					class="glyphicon glyphicon-calendar"></span>
				</span>
			</div>

			<label for="newMeetingEndTimePicker">종료시간</label>
			<div class='input-group date' id='newMeetingEndTimePicker'>
				<input type='text' class="form-control" id="newMeetingEndTime" /> <span
					class="input-group-addon"> <span
					class="glyphicon glyphicon-calendar"></span>
				</span>
			</div>

			<label for="newMeetingAllRooms">회의실</label>
			<div class="form-group" id="newMeetingAllRooms">
				<select class="form-control" id="roomSelectBox">
				</select>
			</div>

			<label for="newMeetingRecurrence">반복설정</label>
			<div class="form-group" id="newMeetingRecurrence">
				<label class="checkbox-inline"><input type="checkbox"
					value="" id="isRecurrence">반복</label> <label><input
					type="radio" name="recurrenceDayOfWeek" data-order=1 disabled>월</label>
				<label><input type="radio" name="recurrenceDayOfWeek"
					data-order=2 disabled>화</label> <label><input type="radio"
					name="recurrenceDayOfWeek" data-order=3 disabled>수</label> <label><input
					type="radio" name="recurrenceDayOfWeek" data-order=4 disabled>목</label>
				<label><input type="radio" name="recurrenceDayOfWeek"
					data-order=5 disabled>금</label> <label><input type="radio"
					name="recurrenceDayOfWeek" data-order=6 disabled>토</label> <label><input
					type="radio" name="recurrenceDayOfWeek" data-order=7 disabled>일</label>
				<label><input type="number" id=recurrenceNumber>반복횟수</label>
			</div>

		</div>
		<button type="submit" class="btn btn-primary">Submit</button>
	</form>
	<div id="timeTableArea"></div>
	<script id="roomList" type="text/x-handlebars-template">
		<div class="content">
		    <div class="container">
		        <div class="row">
		            <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12 text-center mb30">
		            	<h2>{{today}}</h2>
		            </div>
		            <div class="table-responsive">
		                <table class="timetable table table-striped ">
		                    <thead>
		                        <tr class="text-center">
		                            <th scope="col"></th>
		                        {{#each roomNames}}
		                            <th scope="col">{{this}}</th>
		                        {{/each}}
		                        </tr>
		                    </thead>
		                    <tbody>
													{{#each timeTable}}
		                        <tr>
		                            <th scope="row">{{time}}</th>
															{{#each schedule}}
		                            <td>{{this}}</td>
															{{/each}}
		                        </tr>
													{{/each}}
		                    </tbody>
		                </table>
		            </div>
		            <!-- timetable -->
		        </div>
		    </div>
		</div>
	</script>
	<script>
		$(document).ready(function() {
			var meetingRoomListTemplate = Handlebars.compile($("#roomList").html()),
				timeTableTemplate = Handlebars.compile($("#roomList").html());
			
			//HH:mm format
			function minutesOfDay(time) {
				var timeSplit = time.split(":");
				return parseInt(timeSplit[0]) * 60 + parseInt(timeSplit[1]);
			}
			
			function toTimeStr(time) {
				return parseInt(time / 60) + ":" + ("0" + time % 60).slice(-2);
			}
			
			function toReservationText(meeting) {
				return (meeting.title ? meeting.title : "제목없음") 
					+ "( " + meeting.userName + " ~" + meeting.endTime + ")";
			}
			
			function addAll(source, dest) {
				var i;
				for (i = 0; i < source.length; i++) {
					dest.push(source[i]);
				}
			}
			function buildRoomsOccupation(rooms) {
				var i, j, next, roomList = [], meetings = [], timeTable = [], content,
					minStartTime, maxEndTime, roomOrder = {}, startTime, endTime, roomIdx,
					startTimeMinutes, endTimeMinutes;
				if (!rooms || !rooms.length) {
					return [];
				}

				for (i = 0; i < rooms.length; i++) {
					roomList.push(rooms[i].name);
					roomOrder[rooms[i].id] = i;
					if (rooms[i].meetings) {
						addAll(rooms[i].meetings, meetings);
					}
				}
				meetings.sort(function(m1, m2) {
					var compare = m1.startTime.localeCompare(m2.startTime);
					return compare != 0 ? compare : m1.endTime.localeCompare(m2);
				});
				
				if (meetings.length) {
					minStartTime = minutesOfDay(meetings[0].startTime);
					maxEndTime = minutesOfDay(meetings[meetings.length - 1].endTime);
				} else {
					minStartTime = 0;
					maxEndTime = 24 * 60;
				}
				for (i = 0, next = minStartTime; next < maxEndTime; i++, next += 30) {
					timeTable[i] = {
							"schedule" : new Array(rooms.length),
							"time" : toTimeStr(next) 
					}
					for (j = 0; j < rooms.length; j++) {
						timeTable[i].schedule[j] = "";
					}
				}
				for (i = 0; i < meetings.length; i++) {
					startTimeMinutes = minutesOfDay(meetings[i].startTime);
					endTimeMinutes = minutesOfDay(meetings[i].endTime);
					roomIdx = roomOrder[meetings[i].meetingRoom];
					next = startTimeMinutes;
					content = toReservationText(meetings[i]);
					while (next < endTimeMinutes) {
						timeTable[parseInt((next - minStartTime) / 30)].schedule[roomIdx] = content;
						next += 30;
					}
				}
				return {
					"roomNames" : roomList,
					"timeTable" : timeTable
				}
			}
			function setAllMeetingRoom(timeTable) {
				$("#roomSelectBox").html(meetingRoomListTemplate(timeTable))
			}
			function init() {
				var today = new Date(), todayStr = today.getFullYear()
					+ "-" + ("0" + (today.getMonth() + 1)).slice(-2)
					+ "-" + ("0" + today.getDate()).slice(-2);
				
				$.ajax({
					url : "http://localhost:8080/api/meeting?date=" + todayStr,
					crossDomain : true,
					success : function(data) {
						var templateData;
						setAllMeetingRoom(templateData);
						
						templateData = buildRoomsOccupation(data);
						templateData.today = todayStr;
						$("#timeTableArea").html(timeTableTemplate(templateData));
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
						meetingRoom: parseInt($("#roomSelectBox option:selected").data('room-id'))
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