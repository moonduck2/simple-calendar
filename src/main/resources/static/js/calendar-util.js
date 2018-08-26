var CalendarUtil = {
	timeTableTemplate : Handlebars.compile($("#roomList").html()),
	
	//HH:mm format
	minutesOfDay : function(time) {
		var timeSplit = time.split(":");
		return parseInt(timeSplit[0]) * 60 + parseInt(timeSplit[1]);
	},
	toTimeStr : function(time) {
		return parseInt(time / 60) + ":" + ("0" + time % 60).slice(-2);
	},
	generateMeetingRoomOptions : function(roomList) {
		var option = '', i;
		for (i = 0; i < roomList.length; i++) {
			option += ("<option data-room-id=" + roomList[i].id + ">" + roomList[i].name + "</option>");
		}
		return option;
	},
	toReservationText : function(meeting) {
		return (meeting.title ? meeting.title : "제목없음") 
		+ "( " + meeting.userName + " ~" + meeting.endTime + ")";
	},
	addAll : function(source, dest) {
		var i;
		for (i = 0; i < source.length; i++) {
			dest.push(source[i]);
		}
	},
	buildRoomsOccupation : function(rooms) {
		var i, j, prev, next, roomList = [], meetings = [], timeTable = [], content,
		minStartTime, maxEndTime, roomOrder = {}, startTime, endTime, roomIdx,
		startTimeMinutes, endTimeMinutes;
		if (!rooms) {
			rooms = [];
		}

		for (i = 0; i < rooms.length; i++) {
			roomList.push(rooms[i].name);
			roomOrder[rooms[i].id] = i;
			if (rooms[i].meetings) {
				CalendarUtil.addAll(rooms[i].meetings, meetings);
			}
		}

		if (meetings.length) {
			prev = meetings[0];
			minStartTime = prev.startTime;
			maxEndTime = prev.endTime;
			for (i = 1; i < meetings.length; i++) {
				minStartTime = minStartTime.localeCompare(meetings[i].startTime) < 0 ? minStartTime : meetings[i].startTime;
				maxEndTime = maxEndTime.localeCompare(meetings[i].endTime) > 0 ? maxEndTime : meetings[i].endTime;
				prev = meetings[i];
			}
			minStartTime = CalendarUtil.minutesOfDay(minStartTime);
			maxEndTime = CalendarUtil.minutesOfDay(maxEndTime);
		} else {
			minStartTime = 0;
			maxEndTime = 24 * 60;
		}
		for (i = 0, next = minStartTime; next < maxEndTime; i++, next += 30) {
			timeTable[i] = {
					"schedule" : new Array(rooms.length),
					"time" : CalendarUtil.toTimeStr(next) + "~" + CalendarUtil.toTimeStr(next + 30)
			}
			for (j = 0; j < rooms.length; j++) {
				timeTable[i].schedule[j] = {};
			}
		}
		for (i = 0; i < meetings.length; i++) {
			startTimeMinutes = CalendarUtil.minutesOfDay(meetings[i].startTime);
			endTimeMinutes = CalendarUtil.minutesOfDay(meetings[i].endTime);
			roomIdx = roomOrder[meetings[i].meetingRoom];
			next = startTimeMinutes;
			content = CalendarUtil.toReservationText(meetings[i]);
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
	},
	setAllMeetingRoom : function(roomList) {
		$("#roomSelectBox").html(CalendarUtil.generateMeetingRoomOptions(roomList))
	},
	modifyDate : function(dateStr) {
		$("#today h2").text(dateStr);
		$("#today").data("today", dateStr);
	},
	clearHidden : function() {
		$("#meetingIdHidden").val('');
		CalendarUtil.setNew();
	},
	init : function(today) {
		var todayStr = today.getFullYear()
		+ "-" + ("0" + (today.getMonth() + 1)).slice(-2)
		+ "-" + ("0" + today.getDate()).slice(-2);

		$.ajax({
			url : "http://localhost:8080/api/meeting?date=" + todayStr,
			crossDomain : true,
			success : function(data) {
				var templateData;
				CalendarUtil.setAllMeetingRoom(data);

				templateData = CalendarUtil.buildRoomsOccupation(data);
				$("#timeTableArea").html(CalendarUtil.timeTableTemplate(templateData));
				CalendarUtil.modifyDate(todayStr);
				CalendarUtil.clearHidden();
			}
		})	
	},
	clearNewMeeting : function() {
		$("#newMeetingStartDate").val('');
		$("#newMeetingEndDate").val('');
		$("#newMeetingTitle").val('');
		$("#newMeetingContent").val('');
		$("#newMeetingOwner").val('');
		$("#newMeetingStartTime").val('');
		$("#newMeetingEndTime").val('');
	},
	parseDate : function(date) {
		return new Date(parseInt(date.substring(0, 4)), parseInt(date.substring(5, 7)) - 1, 
				parseInt(date.substring(8)));
	},
	prevDate : function(date) {
		return new Date(date.getTime() - 24 * 60 * 60 * 1000)
	},
	nextDate : function(date) {
		return new Date(date.getTime() + 24 * 60 * 60 * 1000)
	},
	currentDate : function() {
		return CalendarUtil.parseDate($("#today").data("today"));
	},
	setUpdate : function() {
		$("#operation").val("put");
	},
	setNew : function() {
		$("#operation").val("post");
	},
	getMethod : function() {
		return $("#operation").val();
	},
	getNewMeeting : function () {
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
	},
	setMeeting : function(meeting) {
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
}
