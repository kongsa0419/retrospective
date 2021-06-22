package serviceImpl;

import domain.User;
import enums.Major;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import repository.UserMapper;
import service.UserService;
import util.JwtUtil;

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
    public User getMyInfo(String rawJwt) {
        int id = jwtUtil.getIdByParsingJwt(rawJwt);
        //if id가 없으면...?
        return mUserMapper.getUserById(id);
    }

    @Override
    public String logInValidation(User user) throws Exception {

        if (user.getEmail() == null) {throw new Exception("email is invalid");}
        if (user.getPw() == null) {throw new Exception("pwd is invalid");}

        int id = mUserMapper.getIdByEmail(user.getEmail());
        User userForCheck = mUserMapper.getUserById(id);
        String inputPwd  = user.getPw();

        if(!inputPwd.equals(userForCheck.getPw())) {throw new Exception("id and pwd doesn't match");}

        return jwtUtil.genJsonWebToken(id);
    }

    /** <null, 중복체크>
     * DB.user에 notnull이 많아서 체크해줘야함
     * 스프링의 isEmpty()는 isBlink():빈칸체크 + null check 해줌  */
    @Override
    public int insertNewUser(User user) {

        /** id, regdate, logintype, promcount, breakcount :: DB에 default값 되어있어서 null체크 필요X
         *  collegeid : 외부인 고려, attendrate, outdt --> nullable
         *  결과적으로 !! nullable -> collegeId, attendrate, outdt */
        if(StringUtils.isEmpty(user.getName()))
            throw new IllegalArgumentException("The name cannot be null / empty");
        if(StringUtils.isEmpty(user.getTel1()))
            throw new IllegalArgumentException("The tel1 cannot be null / empty");
        if(StringUtils.isEmpty(user.getTel2()))
            throw new IllegalArgumentException("The tel2 cannot be null / empty");
        if(StringUtils.isEmpty(user.getTel3()))
            throw new IllegalArgumentException("The tel3 cannot be null / empty");
        if(StringUtils.isEmpty(user.getResidence()))
            throw new IllegalArgumentException("The Residence cannot be null / empty");
        if(StringUtils.isEmpty(user.getLogintype()))
            throw new IllegalArgumentException("The id Logintype be null / empty");
        if(StringUtils.isEmpty(user.getEmail()))
            throw new IllegalArgumentException("The email cannot be null / empty");
        if(StringUtils.isEmpty(user.getPw()))
            throw new IllegalArgumentException("The password(pw) cannot be null / empty");

        if(StringUtils.isEmpty(user.getCollegeid()))
            user.setCollegeid("외부인");

        checkCollegeIdIsValid(user.getCollegeid());

        return mUserMapper.insertNewUser(user);
    }

    //실제로 지우기보다는 outdt 값을 줘서 뺄까
//    @Override
//    public int deleteUser(User user) {
//        int userId = user.getId();
//        String userEmail = user.getEmail();
//        return 0;
//    }


    boolean checkCollegeIdIsValid(String rowClgId) throws IllegalArgumentException{
        rowClgId = rowClgId.trim();
        if(rowClgId.length() != 10)
            throw new IllegalArgumentException("invalid CollegeId. it's length is not 10.");
        if(!rowClgId.chars().allMatch(Character::isDigit))
            throw new IllegalArgumentException("invalid CollegeId. it's not numeric");

        //  2017(입학년도) 136(학과코드) 063(학생번호)로 나눔 -> 입학년도||학과코드||학생번호 가 터무니없으면 invalid
        int entDate = Integer.valueOf(rowClgId.substring(0,4));
        int majorCode = Integer.valueOf(rowClgId.substring(4,7));
        int personId = Integer.valueOf(rowClgId.substring(7,10));
        int majorEntireStudCount = 0;
        int thisYear =  Calendar.getInstance().get(Calendar.YEAR);


        if(entDate<2008 || entDate> thisYear) //터무니없는 입학년도
            throw new IllegalArgumentException("invalid CollegeId. Your entrance Date is incorrect.");
        if(Arrays.stream(Major.values()).noneMatch(i -> i.getMajorCode() == majorCode)) //존재하지 않는 학부코드 ex)233
            throw new IllegalArgumentException("invalid CollegeId. your StdId.MajorCode is incorrect.");
        else{ //학부정원값을 얻고싶음
            List<Major> almajor = Arrays.stream(Major.values()).filter(m->m.getMajorCode()==majorCode).collect(Collectors.toList()); //test 필요. 직접 debug 찍기
            majorEntireStudCount = almajor.get(0).getEntireStudentCount();
        }
        if(personId==0 || personId > majorEntireStudCount)
            throw new IllegalArgumentException("invalid CollegeId. your StdId.personId(last 3 chars) is incorrect.");

        return true;
    }



}
