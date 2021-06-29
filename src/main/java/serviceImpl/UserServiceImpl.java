package serviceImpl;

import domain.User;
import enums.Major;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import repository.GuestMapper;
import repository.UserMapper;
import service.UserService;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mUserMapper;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public User getMe() throws IllegalAccessException {
        long id = getLoginUserId();
        return mUserMapper.getUserById(id);
    }

    @Override
    public String updateUser(User user) {
        mUserMapper.updateUser(user);
        return "user 정보를 업데이트 했습니다.";
    }

    //soft delete 고려할것
    @Override
    public String deleteUser() {
        Long userId = getLoginUserId();
        mUserMapper.deleteUser(userId);
        return "userId[" + userId + "] soft deleted.";
    }

    @Override
    public String findPassword() {
        Long userId = getLoginUserId();
        User temp = mUserMapper.getUserById(userId);
        return temp.getPw();
    }

    /*
    @Override
    public String autoUpdateUserAttendrate(long guestTBLId) {
        long guestId = mGuestMapper.getUserIdById(guestTBLId);
        long rsvId = mGuestMapper.getReservationIdById(guestTBLId);
        // 예약 종료시간이 지나면 auto Update
        return "경기가 종료되었고, user의 참석률이 반영되었습니다. 별다른 사유가 없었다면 출석이 인정됩니다.";
    }
    */

    public long getLoginUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        long loginId = jwtUtil.getParsedIdIfJwtValid(token);
        return loginId;
    }

    @Override
    public String signIn(User user) throws Exception {

        if (user.getEmail() == null) {
            throw new IllegalArgumentException("email is invalid");
        }
        if (user.getPw() == null) {
            throw new IllegalArgumentException("pwd is invalid");
        }

        long id = mUserMapper.getIdByEmail(user.getEmail());
        User userForCheck = mUserMapper.getUserById(id);
        String inputPwd = user.getPw();

        if (!inputPwd.equals(userForCheck.getPw())) {
            throw new Exception("id and pwd doesn't match");
        }

        return jwtUtil.genJsonWebToken(id);
    }

    /**
     * <null, 중복체크>
     * DB.user에 notnull이 많아서 체크해줘야함
     * 스프링의 isEmpty()는 isBlink():빈칸체크 + null check 해줌
     */
    @Override
    public String signUp(User user) {

        /** id, regdate, logintype, promcount, breakcount :: DB에 default값 되어있어서 null체크 필요X
         *  collegeid : 외부인 고려, attendrate, outdt --> nullable
         *  결과적으로 !! nullable -> collegeId, attendrate, outdt */
        if (StringUtils.isEmpty(user.getName()))
            throw new IllegalArgumentException("The name cannot be null / empty");
        if (StringUtils.isEmpty(user.getTel()))
            throw new IllegalArgumentException("The tel cannot be null / empty");
        if (StringUtils.isEmpty(user.getResidence()))
            throw new IllegalArgumentException("The Residence cannot be null / empty");
        if (StringUtils.isEmpty(user.getLogintype()))
            throw new IllegalArgumentException("The id Logintype be null / empty");
        if (StringUtils.isEmpty(user.getEmail()))
            throw new IllegalArgumentException("The email cannot be null / empty");
        if (StringUtils.isEmpty(user.getPw()))
            throw new IllegalArgumentException("The password(pw) cannot be null / empty");
        if (StringUtils.isEmpty(user.getStdid()))
            user.setStdid("외부인");

        //학번 체크
        if(!isStdIdExist(user.getStdid())){
            checkStudentIdIsValid(user.getStdid());
        }else{
            throw new IllegalArgumentException("학번이 이미 존재합니다.");
        }

        //전화번호체크
        //이메일체크

        mUserMapper.signUp(user);
        return "회원가입 성공";

    }




    boolean checkStudentIdIsValid(String rowstdid){
        rowstdid = rowstdid.trim();
        try{
            if (rowstdid.length() != 10)
                throw new IllegalArgumentException("invalid CollegeId. it's length is not 10.");
            if (!rowstdid.chars().allMatch(Character::isDigit))
                throw new IllegalArgumentException("invalid CollegeId. it's not numeric");
        }catch (IllegalArgumentException exception){
            exception.printStackTrace();
            return false;
        }

        //  2017(입학년도) 136(학과코드) 063(학생번호)로 나눔 -> 입학년도||학과코드||학생번호 가 터무니없으면 invalid
        int entDate = Integer.valueOf(rowstdid.substring(0, 4));
        int majorCode = Integer.valueOf(rowstdid.substring(4, 7));
        int personId = Integer.valueOf(rowstdid.substring(7, 10));
        int majorEntireStudCount = 0;
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        try{
            if (entDate < 2008 || entDate > thisYear) //터무니없는 입학년도
                throw new IllegalArgumentException("invalid CollegeId. Your entrance Date is incorrect.");
            if (Arrays.stream(Major.values()).noneMatch(i -> i.getMajorCode() == majorCode)) //존재하지 않는 학부코드 ex)233
                throw new IllegalArgumentException("invalid CollegeId. your StdId.MajorCode is incorrect.");
            else { //학부정원값을 얻고싶음
                List<Major> almajor = Arrays.stream(Major.values()).filter(m -> m.getMajorCode() == majorCode).collect(Collectors.toList()); //test 필요. 직접 debug 찍기
                majorEntireStudCount = almajor.get(0).getEntireStudentCount();
            }
            if (personId == 0 || personId > majorEntireStudCount)
                throw new IllegalArgumentException("invalid CollegeId. your StdId.personId(last 3 chars) is incorrect.");
        }catch (IllegalArgumentException exception){
            exception.printStackTrace();
            return false;
        }

        return true;
    }


    // TODO: 참석수-update ,{pw,tel,name} update , 비번찾기, 이메일중복,
    public boolean isStdIdExist(String input) {
        long existId = mUserMapper.getIdByStdId(input);
        return existId >= 0;
    }

}
