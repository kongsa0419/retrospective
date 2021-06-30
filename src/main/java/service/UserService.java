package service;

import domain.User;

import java.sql.Timestamp;
import java.util.Optional;

//기능위주
public interface UserService {
    //crud, String으로 반환하는 이유는
    String signIn(User user) throws Exception; //login
    String signUp(User user); //회원가입
    User getMe() throws IllegalAccessException; // 조회
    User getUserIfExists(long userId);
    String updateUser(User user); //갱신
    String deleteUser(); //회원탈퇴
    long getLoginUserId();

    boolean isUserWithdrew(long userId);
    boolean isStdIdExist(String stdId);
    String findPassword(); //email을 받을수는 없잖아

//    String autoUpdateUserAttendrate(long guestTBLId); //Geust에 참가 됐을 때 해줄것

//    boolean isTelExist(String new_tel);
//    boolean isEmailExist(String new_email);
}
