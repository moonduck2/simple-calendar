<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Meeting room</title>

<link rel="stylesheet" href="resources/static/css/bootstrap-4.1.3.min.css">
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
							<input type="hidden" id="operation" value="post">
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
	
	<script type="text/javascript" src="resources/static/js/popper-1.14.3.min.js"></script>
	<script type="text/javascript" src="resources/static/js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="resources/static/js/bootstrap-4.1.3.min.js"></script>
	<script type="text/javascript" src="resources/static/js/handlebars-4.0.11.min.js"></script>
	<script type="text/javascript" src="resources/static/js/calendar-util.js"></script>
	<script type="text/javascript" src="resources/static/js/calendar-event-handler.js"></script>
</body>
</html>