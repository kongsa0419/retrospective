package repository;

import domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    User getUserById(int id);

    int getIdByEmail(String email);

    int getIdByPwd(String pwd);

    int insertNewUser(User uesr);
}
