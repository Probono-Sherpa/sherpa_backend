package com.eastflag.nnc.schedule;

import com.eastflag.nnc.schedule.scheduleexception.ScheduleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp ;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Component
@RequiredArgsConstructor
public class ScheduleDaoService {

    // 여기서 DB 연결 작업
    //private static List<Schedule> sdl = new ArrayList<>(); //Sche Dule List
//    private static int user_id = 0;
//    private static int schedule_id =0;
//    private static int route_id = 0;
//
//    static {
//        sdl.add(new Schedule(++schedule_id,++user_id,"Apple", new Timestamp (System.currentTimeMillis()).toString(), new Timestamp(System.currentTimeMillis()).toString(), "과일사기", ++route_id));
//        sdl.add(new Schedule(++schedule_id,user_id,"Apple11", new Timestamp (System.currentTimeMillis()).toString(), new Timestamp(System.currentTimeMillis()).toString(), "과일사기11", ++route_id));
//        sdl.add(new Schedule(++schedule_id,user_id,"Apple22", new Timestamp (System.currentTimeMillis()).toString(), new Timestamp(System.currentTimeMillis()).toString(), "과일사기22", ++route_id));
//        sdl.add(new Schedule(++schedule_id,++user_id,"Banana", new Timestamp (System.currentTimeMillis()).toString(), new Timestamp(System.currentTimeMillis()).toString(), "과일사기", ++route_id));
//        sdl.add(new Schedule(++schedule_id,++user_id,"Orange", new Timestamp (System.currentTimeMillis()).toString(), new Timestamp(System.currentTimeMillis()).toString(), "과일사기", ++route_id));
//    }
    private final ScheduleRepository sdl;

    public Schedule saveSchedule(Schedule schedule){

        sdl.save(schedule);

        return schedule;
    }

    public List<Schedule> getAllSchedule(int user_id){

        return sdl.findScheduleByUserId(user_id);
    }

    // 값을 찾는 로직 - shceduleID 이용
    public Schedule findScheduleByScheduleID(int scheduleID){

        return sdl.findById(scheduleID)
                .orElseThrow(() -> new ScheduleNotFoundException("Not Found Schedule ID"));
    }

    // 값을 찾는 로직 - Datetime 이용
    public List<Schedule> getScheduleByDateTime(int userID, String dateTime) throws UnsupportedEncodingException {
        dateTime = URLDecoder.decode(dateTime, StandardCharsets.UTF_8);

        Timestamp check = Timestamp.valueOf(dateTime);

        Predicate<Schedule> findDate = sc-> check.getTime() >= Timestamp.valueOf(sc.getDateBegin()).getTime() && check.getTime() <= Timestamp.valueOf(sc.getDateEnd()).getTime();

        List<Schedule> target = sdl.findScheduleByUserId(userID);

        target = target.stream().filter(findDate).collect(Collectors.toList());

        return target;
    }

    // schedule 삭제
    public Schedule removeSchedule(int scheduleID) {
        var ret = findScheduleByScheduleID(scheduleID);

        sdl.deleteById(scheduleID);

        return ret;
    }

    //schedule 수정
    public Schedule modify(Schedule schedule) {
        var target = findScheduleByScheduleID(schedule.getScheduleId());

        target.setTitle(schedule.getTitle());
        target.setDescription(schedule.getDescription());
        target.setDateBegin(schedule.getDateBegin());
        target.setDateEnd(schedule.getDateEnd());

        sdl.save(target);

        return target;
    }
}