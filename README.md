# 빌드
```bash
    $> git clone https://github.com/moonduck2/simple-calendar.git calendar
    $> cd calendar
    $> ./mvnw package
```

# 실행

# 문제 해결 전략

## 개요
이 어플리케이션을 개발하기 위해서는 다음 두 가지에 대한 처리가 필요하다.
#### 반복일정 처리  
1. 시작일과 종료일이 정해진 어플리케이션이므로 시작일, 종료일, 회의시간 등을 저장하는 Table을 만든다.  
2. 새 회의(혹은 수정된 회의)와 겹칠 만한 구간의 회의를 전부 다 가져온다. 
```sql
    select m from Meeting m left outer join Recurrence r where 
        m.meetingRoom = :meetingRoom and /*예약하려는 회의실 중에*/
        ((m.startDate <= :newStartDate and m.endDate >= :newStartDate) or /*회의 날짜가 겹쳐야하며*/
        (m.startDate <= :newEndDate and m.endDate >= :newEndDate)) and
        ((m.startTime <= :newStartTime and m.endTime > :newStartTime) or /*회의 시간이 겹쳐야 한다, 단 끝나는시간이나 시작시간이 겹치는건 상관 없다*/
        (m.startTime < :newEndTime and m.endTime >= :newEndTime))
```
3. 2에서 가져온 모든 회의 일정이 모두 겹친다고 볼 수는 없다. 특정 요일만 반복하는 경우 해당 구간 전체일이 다 회의가 있지 않기 때문이다. 그래서 이 쿼리 결과는 겹치는 회의가 아니고, "겹칠만한" 회의이다. 따라서 2에서 조회된 회의에서 실제로 새 회의 예약과 겹치는 것이 있는지 테스트한다. RecurrenceHandler 인터페이스를 통해 다양한 반복 설정에 따른(현재는 특정 요일만 반복과 반복 없을 경우) 특정일에 회의 발생 여부를 확인할 수 있다. Pseudo 코드는 아래와 같다.
```
Meeting newMeeting;
List<Meeting> likelyOccupieds; //newMeeting과 날짜/시간이 겹칠만한 회의들

for each likelyOccupied in likelyOccupieds
do
    //겹치는 날짜 사이에서만 조회하기 위해
    startDate = Max(startDateOfNewMeeting, startDateOfLikelyOccupied) 
    endDate = Min(endDateOfNewMeeting, endDateOfLikelyOccupied)
    
    Date nextOccurDate = startDate
    while (nextOccurDate <= endDate)
    do 
    //nextOccurDate에 해당 회의가 둘 다 개최되었는지 확인
    if (recurrenceHandler.isOccur(likelyOccupied, nextOccurDate) && recurrenceHandler.isOccur(newMeeting, nextOccurDate)
        return true
    //다음 겹칠만한 회의를 찾는다. 각각 다음 회의일을 계산해서 큰 값을 취한다(더 많이 건너띄기 위해)
    nextOccurOfNewMeeting = recurrenceHandler.nextOccur(nextOccurDate, nextOccurOfNewMeeting)
    nextOccurOflikelyOccupied = recurrenceHandler.nextOccur(nextOccurDate, nextOccurOflikelyOccupied)
    nextOccurDate = Max(nextOccurOfNewMeeting, nextOccurOflikelyOccupied)
    done
done
```
4. 겹치는 일정이 있으면 회의 생성을 중지하고 rollback한다.

#### 동시 예약 처리
1. 회의 table에 enabled와 modified_time 필드를 추가한다. 회의 Table은 아래와 같은 칼럼들을 가지고 있다.
```sql
id integer primary key,
start_date date not null,
end_date date not null,
start_time time not null,
end_time time not null,
enabled boolean not null, //enabled가 true일 때만 조회가 된다.
modified time long not null //먼저 예약한 것임을 확인하기 위한 column이다.
    
```
2. 예약 버튼을 눌러 예약을 하게 되면 기본적으로 enabled = false가 된 상태로 회의를 만든다. enabled=false이기때문에 조회되지 않는다.
3. 추가한 회의와 겹칠만한 회의를 모두 가져온다(위의 반복 처리 참고)
4. 겹칠만한 회의(새로 넣은 회의 포함)를 modified_time이 작은 순으로 정렬한다.
5. modified_timne이 가장 적은 회의가 새로 넣은 회의와 일치하면 예약 성공이다. enabled를 true로 한다
6. 그렇지 않을 경우 예외를 발생시켜 rollback 처리한다.
7. 예시  
| id | start_date | end_date | start_time | end_time | enabled | modified_time |
|----|------------|----------|------------|----------|---------|---------------|
| 1 | 2018-08-01  |2018-08-05| 10:00 | 12:00 | false | 1 |
| 2 | 2018-08-03  |2018-08-07| 11:00 | 13:00 | false | 2 |
위 테이블에서 동시요청으로 인해 id가 1, 2인 row가 생성되었고, 8/3~7 11~12시 회의가 겹친다. 그러나 id가 1인 회의는 modified_time이 더 적으므로 성공적으로 commit되며, 2를 추가하려는 행위는 롤백되어 최종적으로 삭제된다.

##### 예약 수정하기
수정하기는 단순히 property값을 수정하지 않고 새로 생성 후 기존 회의를 삭제하는 방법으로 한다. 과정은 아래와 같다.
1. 수정된 예약으로 새 회의 예약을 한다. 과정은 위의 동시 예약 처리와 동일하다.
2. 이미 회의가 있는 시간대이면 1에서 예외가 발생되어 rollback된다.
3. 새로 요청한 시간대가 기존에 회의가 없다면 기존 회의를 삭제하고 새 회의를 enabled = true로 변경한다.