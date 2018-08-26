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
	<div id="controlBoxArea" class="container">
		<div class="row">
			<div class="col-xs">
				<button id="prevDate" type="button" class="btn btn-info">어제</button>
			</div>
			<div id="today" class="col-xl text-center" data-today="">
				   	<h2></h2>
		  </div>
		  <div class="col-xs">
		  	<button id="nextDate" type="button" class="btn btn-info">내일</button>
		  </div>
			<div class="col-4">
				<form id="registerRoom">
					<div class="row"> 
						<div class="col">
							<input type="text" class="form-control" id="newRoomName" placeholder="새 회의실">
						</div>
						<div class="col-xs">
							<button type="submit" class="btn btn-primary">Submit</button>
						</div>
					</div>
				</form>
			</div>
		</div>
		<div class="row align-items-start">
			<div id="timeTableArea" class="col-xl"></div>
			
			<div id="registerMeetingArea" class="col-4">
				<form id="registerMeeting">
					<div class="row">
						<div class="col">
							<label for="newMeetingOwner">주최자</label>
							<input class="form-control"	id="newMeetingOwner"> 
						</div>
						<div class="col">
							<label for="newMeetingAllRooms">회의실</label>
							<div class="form-group" id="newMeetingAllRooms">
								<select class="form-control" id="roomSelectBox">
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class = "col">
							<label for="newMeetingStartDatePicker">시작일</label>
							<div class='input-group date' id='newMeetingStartDatePicker'>
								<input type='text' class="form-control" id="newMeetingStartDate" />
								<span class="input-group-addon"> <span
									class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
						</div>
						<div class="col">
							<label for="newMeetingEndDatePicker">종료일</label>
							<div class='input-group date' id='newMeetingEndDatePicker'>
								<input type='text' class="form-control" id="newMeetingEndDate" /> 
								<span	class="input-group-addon"> 
									<span	class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
						</div>
						<div class="col">
							<label for="newMeetingStartTimePicker">시작시간</label>
							<div class='input-group date' id='newMeetingStartTimePicker'>
								<input type='text' class="form-control" id="newMeetingStartTime" />
								<span class="input-group-addon"> 
									<span	class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
						</div>
						<div class="col">
							<label for="newMeetingEndTimePicker">종료시간</label>
							<div class='input-group date' id='newMeetingEndTimePicker'>
								<input type='text' class="form-control" id="newMeetingEndTime" /> 
								<span	class="input-group-addon"> 
									<span	class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col">
							<label for="newMeetingTitle">회의명</label> 
							<input type="text" class="form-control" id="newMeetingTitle" placeholder="Enter meeting title">
						</div> 
					</div>
					<div class="row">
						<div class="col">
							<label for="newMeetingContent">회의 요약</label>
							<textarea class="form-control" rows="5" id="newMeetingContent"></textarea>
						</div>
					</div>
					<div class="row">
						<div class="col">
							<label class="checkbox-inline">
								<input type="checkbox"	value="" id="isRecurrence">반복
							</label>
						</div>
						<div class="col">
							<div class="col">
								<input type="number" class="form-control" id="recurrenceNumber" placeholder="횟수">
							</div>
						</div>
					</div>
					<div class="row" id="newMeetingRecurrence">
						<div class="col">
							<div class="row">
								<div class="col">
									<label><input	type="radio" name="recurrenceDayOfWeek" data-order=1 disabled checked>월</label>
								</div>
								<div class="col">
									<label><input type="radio" name="recurrenceDayOfWeek"	data-order=2 disabled>화</label>
								</div>
								<div class="col"> 
									<label><input type="radio" name="recurrenceDayOfWeek" data-order=3 disabled>수</label>
								</div>
								<div class="col"> 
									<label><input	type="radio" name="recurrenceDayOfWeek" data-order=4 disabled>목</label>
								</div>
								<div class="col">
									<label><input type="radio" name="recurrenceDayOfWeek"	data-order=5 disabled>금</label>
								</div>
								<div class="col"> 
									<label><input type="radio" name="recurrenceDayOfWeek" data-order=6 disabled>토</label>
								</div>
								<div class="col">
									<label><input	type="radio" name="recurrenceDayOfWeek" data-order=7 disabled>일</label>
								</div>
							</div>
						</div>
					</div>
					<div class="row text-center">
						<div class="col">
							<input type="hidden" id="meetingIdHidden">
							<button type="submit" class="btn btn-primary">Submit</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script id="roomList" type="text/x-handlebars-template">
		<div class="content">
		    <div class="container">
		        <div class="row align-items-end">
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
		                            <td>
																		<div>{{content}}</div>
																{{#if meeting}}
																		<div class="row">
																			<div class="col"><button type="button" data-meeting-id={{meeting}} class="btn btn-warning modifyMeeting">수정</button></div>
																			<div class="col"><button type="button" data-meeting-id={{meeting}} class="btn btn-danger deleteMeeting">삭제</button></div>
																		</div>
																{{/if}}
																</td>
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
			var timeTableTemplate = Handlebars.compile($("#roomList").html());
			
			//HH:mm format
			function minutesOfDay(time) {
				var timeSplit = time.split(":");
				return parseInt(timeSplit[0]) * 60 + parseInt(timeSplit[1]);
			}
			
			function toTimeStr(time) {
				return parseInt(time / 60) + ":" + ("0" + time % 60).slice(-2);
			}
			function generateMeetingRoomOptions(roomList) {
				var option = '', i;
				for (i = 0; i < roomList.length; i++) {
					option += ("<option data-room-id=" + roomList[i].id + ">" + roomList[i].name + "</option>");
				}
				return option;
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
				if (!rooms) {
					rooms = [];
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
							"time" : toTimeStr(next) + "~" + toTimeStr(next + 30)
					}
					for (j = 0; j < rooms.length; j++) {
						timeTable[i].schedule[j] = {};
					}
				}
				for (i = 0; i < meetings.length; i++) {
					startTimeMinutes = minutesOfDay(meetings[i].startTime);
					endTimeMinutes = minutesOfDay(meetings[i].endTime);
					roomIdx = roomOrder[meetings[i].meetingRoom];
					next = startTimeMinutes;
					content = toReservationText(meetings[i]);
					while (next < endTimeMinutes) {
						timeTable[parseInt((next - minStartTime) / 30)].schedule[roomIdx].content = content;
						timeTable[parseInt((next - minStartTime) / 30)].schedule[roomIdx].meeting = meetings[i].id;
						next += 30;
					}
				}
				return {
					"roomNames" : roomList,
					"timeTable" : timeTable
				}
			}
			function setAllMeetingRoom(roomList) {
				$("#roomSelectBox").html(generateMeetingRoomOptions(roomList))
			}
			function modifyDate(dateStr) {
				$("#today h2").text(dateStr);
				$("#today").data("today", dateStr);
			}
			function clearHidden() {
				$("#meetingIdHidden").val('');
			}
			function init(today) {
				var todayStr = today.getFullYear()
					+ "-" + ("0" + (today.getMonth() + 1)).slice(-2)
					+ "-" + ("0" + today.getDate()).slice(-2);
				
				$.ajax({
					url : "http://localhost:8080/api/meeting?date=" + todayStr,
					crossDomain : true,
					success : function(data) {
						var templateData;
						setAllMeetingRoom(data);
						
						templateData = buildRoomsOccupation(data);
						$("#timeTableArea").html(timeTableTemplate(templateData));
						modifyDate(todayStr);
						clearHidden();
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
			function parseDate(date) {
				return new Date(parseInt(date.substring(0, 4)), parseInt(date.substring(5, 7)) - 1, 
						parseInt(date.substring(8)));
			}
			function prevDate(date) {
				return new Date(date.getTime() - 24 * 60 * 60 * 1000)
			}
			function nextDate(date) {
				return new Date(date.getTime() + 24 * 60 * 60 * 1000)
			}
			function currentDate() {
				return parseDate($("#today").data("today"));
			}
			function getNewMeeting() {
				var data = {
						startDate : $("#newMeetingStartDate").val(),
						endDate : $("#newMeetingEndDate").val(),
						title : $("#newMeetingTitle").val(),
						content : $("#newMeetingContent").val(),
						userName : $("#newMeetingOwner").val(),
						startTime : $("#newMeetingStartTime").val(),
						endTime : $("#newMeetingEndTime").val(),
						meetingRoom: parseInt($("#roomSelectBox option:selected").data('room-id'))
				}
				if ($("#meetingIdHidden").val()) {
					data.id = parseInt($("#meetingIdHidden").val());
				}
				if($("#isRecurrence").is(":checked")) {
					data.recurrence = {
							dayOfWeek : $("#newMeetingRecurrence input:radio:checked").data("order"),
							type : "ONCE_A_WEEK",
							count : parseInt($("#recurrenceNumber").val())
					}
				}
				return data;
			};
			function setMeeting(meeting) {
				var radios;
				$("#newMeetingStartDate").val(meeting.startDate),
				$("#newMeetingEndDate").val(meeting.endDate),
				$("#newMeetingTitle").val(meeting.title),
				$("#newMeetingContent").val(meeting.content),
				$("#newMeetingOwner").val(meeting.userName),
				$("#newMeetingStartTime").val(meeting.startTime),
				$("#newMeetingEndTime").val(meeting.endTime),
				$("#roomSelectBox [data-room-id=" + meeting.meetingRoom +"]")
				
				$("#meetingIdHidden").val(meeting.id)
				if (meeting.recurrence) {
					$("#isRecurrence").attr("checked", true)
					if (meeting.recurrence.dayOfWeek) {
						
						$("#newMeetingRecurrence input:radio").attr("disabled", false)
						$("#newMeetingRecurrence input:radio[data-order=" + meeting.recurrence.dayOfWeek + "]").attr("checked", true);
					}
					if (meeting.recurrence.count) {
						$("#recurrenceNumber").val(meeting.recurrence.count)
					}
				}
			}
			$("#timeTableArea").on("click", ".modifyMeeting", function(e) {
				$.ajax({
					url : "http://localhost:8080/api/meeting/" + $(this).data("meetingId"),
					success : function(meeting) {
						setMeeting(meeting)
					},
					error : function(xhr, textStatus, errorThrown) {
						alert(xhr.responseJSON.message);
					}
				})
			})
			$("#timeTableArea").on("click", ".deleteMeeting", function(e) {
				$.ajax({
					url : "http://localhost:8080/api/meeting/" + $(this).data("meetingId"),
					method : "delete",
					success : function(data) {
						init(currentDate());
					},
					error : function(xhr, textStatus, errorThrown) {
						alert(xhr.responseJSON.message)
					}
				})
			})
			$("#prevDate").click(function(e) {
				var curDate = parseDate($("#today").data("today"));
				init(prevDate(curDate));
			});
			$("#nextDate").click(function(e) {
				var curDate = parseDate($("#today").data("today"));
				init(nextDate(curDate));
			});
			$("#registerMeeting").submit(function(e) {
				e.preventDefault();
				var data = getNewMeeting();
				$.ajax({
					url : "http://localhost:8080/api/meeting",
					method : "post",
					contentType : "application/json;charset=utf-8",
					dataType : 'json',
					data : JSON.stringify(data),
					success : function(data) {
						clearNewMeeting()
						init(currentDate());
					},
					error : function(xhr, textStatus, errorThrown) {
						if (xhr.responseJSON.message === "선점실패") {
							alert("이미 예약된 시간입니다");
							
							init(currentDate());
						}
					}
				})
				return false;
			})
			$("#registerRoom").submit(function(e) {
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
						init(currentDate());
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
			init(new Date());
		})
	</script>
</body>
</html>