package serviceImpl;

import annotation.Auth;
import domain.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import repository.ReservationMapper;
import service.ReservationService;
import service.UserService;

import javax.annotation.Resource;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/** Auth가 온전히 작동되면, 내 예약에 한해서만 지워야함.
 *
 * */
@Service("ReservationServiceImpl")
public class ReservationServiceImpl implements ReservationService {


    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService mUserService;

    @Autowired
    private ReservationMapper mReservationMapper;



    @Auth
    @Override
    public List<Reservation> getListIHosted() {
        long id = mUserService.getLoginUserId();
        List<Reservation> mylist = new ArrayList<>();
        mylist = mReservationMapper.getRsvListByHostId(id);
        //이상한지 체크할것
        return mylist;
    }

    @Override
    public Reservation getReservation(long rsvId) {
        Reservation rsv = mReservationMapper.getReservationByRsvId(rsvId);
        //이상체크
        return rsv;
    }

    /** 1. host가 fk이기 때문에 UserMapper.getUserById(rsv.getHost()).getOutdt() == null 이어야
     * 정상적으로 insert 가능
     * 2. timestamp 관련해서...ㄱㄷ
     * */
    @Override
    public String createNewReservation(Reservation rsv) {
        //탈퇴한 회원이라면
//        if(mUserService.isUserWithdrew(rsv.getHost())) {
//            throw new IllegalArgumentException("새 예약의 host가 될 사람이 존재하지 않습니다.");
//        }

        //최소한의 null 체크
        if(StringUtils.isEmpty(rsv.getTitle()))
            throw new IllegalArgumentException("예약을 잡으시려면 최소한 제목은 적어주셔야 합니다.");
        if(StringUtils.isEmpty(rsv.getRsvstart())  || StringUtils.isEmpty(rsv.getRsvend()))
            throw new IllegalArgumentException("예약시간대를 정해주셔야 합니다.");

        // 시간대가 올바른지(8~22시 사이), 다른 사용자와 시간대가 겹치지는 않는지 체크
        if(!isPossibleRsvTime(rsv.getRsvstart(), rsv.getRsvend())){
            return "입력된 시간대가 올바르지 않아서 발생한 에러입니다.";
        }

        //모든 예외를 통과하면 쿼리
        mReservationMapper.createNewRsv(rsv);
        return "새 예약을 성공리에 잡았습니다.";
    }


    //동시간대에 다른 사람이 예약중이면 예약불가능
    //https://binshuuuu.tistory.com/187
    public boolean checkRsvNotRepeated(Timestamp start, Timestamp end){
        int count = mReservationMapper.getCountRepeatedTime(start, end);
        if(count>=0)
            return false;
        else
            return true;
    }

    public boolean isPossibleRsvTime(Timestamp ts_start, Timestamp ts_end){
        Calendar start =  Calendar.getInstance();
        start.setTime(ts_start);
        Calendar end = Calendar.getInstance();
        end.setTime(ts_end);
        if(start.get(Calendar.HOUR_OF_DAY)<= 8 || end.get(Calendar.HOUR_OF_DAY) >= 22)
            throw new IllegalArgumentException("주민 민원으로 인해서 08~22시 사이로 이용시간을 제한합니다.");

        long time_diff = (end.getTimeInMillis()-start.getTimeInMillis())/1000; //  (millSeconds -> seconds)
        if(time_diff<=0)
            throw new IllegalArgumentException("예약 시간이 0보다 작거나 같습니다. 잘못되었으니 고쳐주세요.");

        //시간표상 예약시간대가 겹치는지 확인
        if(!checkRsvNotRepeated(ts_start, ts_end))
            throw new IllegalArgumentException("다른 사용자와 예약 시간대가 겹칩니다.");

        return true;
    }

    @Override
    public String updateReservation(Reservation rsv) {
        if(StringUtils.isEmpty(rsv.getId()))
            throw new IllegalArgumentException("건네받은 Reservation의 id가 없어서 update(PUT)할 수 없습니다.");
        mReservationMapper.updateRsv(rsv);
        return "[PUT]예약 정보를 성공적으로 변경했습니다.";
    }

    @Override
    public String deleteReservation(long rsvId) {
        //만약 이미 지워져있다면(표시: notnull)
        if(! StringUtils.isEmpty(mReservationMapper.getReservationByRsvId(rsvId).getIs_deleted()))
            throw new IllegalArgumentException("이미 지워진 예약을 지우려고해서 발생한 에러입니다.");

        mReservationMapper.deleteRsvById(rsvId); //softDelete!
        return "(softly) [" + rsvId + "]번 예약을 성공적으로 지웠습니다.";
    }
}
