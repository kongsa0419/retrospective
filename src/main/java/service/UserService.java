package service;

import domain.User;

public interface UserService {

    User getMyInfo(String rawJwt);

    String logInValidation(User user) throws Exception;

    int insertNewUser(User user);

//    int deleteUser(User user);
}
