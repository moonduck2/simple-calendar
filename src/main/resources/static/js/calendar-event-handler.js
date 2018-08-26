$(document).ready(function() {
	$("#timeTableArea").on("click", ".modifyMeeting", function(e) {
		$.ajax({
			url : "http://localhost:8080/api/meeting/" + $(this).data("meetingId"),
			success : function(meeting) {
				CalendarUtil.setMeeting(meeting)
				$("#operation").val("put");//수정
			},
			error : function(xhr, textStatus, errorThrown) {
				alert(xhr.responseJSON.message);
			}
		});
	});
	
	$("#timeTableArea").on("click", ".deleteMeeting", function(e) {
		$.ajax({
			url : "http://localhost:8080/api/meeting/" + $(this).data("meetingId"),
			method : "delete",
			success : function(data) {
				CalendarUtil.init(CalendarUtil.currentDate());
			},
			error : function(xhr, textStatus, errorThrown) {
				alert(xhr.responseJSON.message)
			}
		})
	})
	$("#meetingAbort").click(function(e) {
		e.preventDefault()
		CalendarUtil.clearNewMeeting();
	})
	$("#prevDate").click(function(e) {
		var curDate = CalendarUtil.parseDate($("#today").data("today"));
		CalendarUtil.init(CalendarUtil.prevDate(curDate));
	});
	$("#nextDate").click(function(e) {
		var curDate = CalendarUtil.parseDate($("#today").data("today"));
		CalendarUtil.init(CalendarUtil.nextDate(curDate));
	});
	$("#registerMeeting").submit(function(e) {
		e.preventDefault();
		var data = CalendarUtil.getNewMeeting();
		$.ajax({
			url : "http://localhost:8080/api/meeting",
			method : CalendarUtil.getMethod(),
			contentType : "application/json;charset=utf-8",
			dataType : 'json',
			data : JSON.stringify(data),
			success : function(data) {
				CalendarUtil.clearNewMeeting()
				CalendarUtil.init(CalendarUtil.currentDate());
			},
			error : function(xhr, textStatus, errorThrown) {
				if (xhr.responseJSON.message === "선점실패") {
					alert("이미 예약된 시간입니다");

					CalendarUtil.init(CalendarUtil.currentDate());
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
				CalendarUtil.init(CalendarUtil.currentDate());
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
	CalendarUtil.init(new Date());
})