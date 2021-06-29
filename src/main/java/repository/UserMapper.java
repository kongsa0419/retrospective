package repository;

import domain.User;
import org.springframework.stereotype.Repository;

// 자잘한 처리 위주
@Repository
public interface UserMapper {
    User getUserById(long id);

    long getIdByEmail(String email);

    long getIdByPwd(String pwd);

    long getIdByStdId(String stdid);

    int signUp(User user);

    int updateUser(User user);

    //soft delete : outdt를 null이 안되게끔 함. --> outdt가 null이면 탈퇴처리된거로 간주
    int deleteUser(long userId);

//    int autoUpdateUserAttendrate(long userId);


}
