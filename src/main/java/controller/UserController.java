package controller;


import annotation.Auth;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;
import util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/user")
@Controller
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService mUserService;

    //토큰이 있어야만 사용가능한 api (로그인 상태). 헤더의 jwt를 파싱하므로써 데이터를 가져오는거지, 클라이언트에게 따로 body나 url로 request를 바라지 않는다.
    @Auth
    @ResponseBody
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public ResponseEntity getMe () throws IllegalAccessException {
        return new ResponseEntity(mUserService.getMe(), HttpStatus.OK);
    }

    @Auth
    @ResponseBody
    @RequestMapping(value = "/me", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(){
        return new ResponseEntity(mUserService.deleteUser(), HttpStatus.OK);
    }



    //로그인 API : 맞으면 token을 주고 틀리면 wrong-message 리턴
    @ResponseBody
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity signIn(@RequestBody User user) throws Exception {
        return new ResponseEntity(mUserService.signIn(user), HttpStatus.OK);
    }


    //회원가입
    @ResponseBody
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity signUp(@RequestBody User user) throws Exception {
        return new ResponseEntity(mUserService.signUp(user),HttpStatus.OK);
    }

    //정보 수정(+비번 수정)
    @Auth
    @ResponseBody
    @RequestMapping(value = "/me", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@RequestBody User user){
        return new ResponseEntity(mUserService.updateUser(user), HttpStatus.OK);
    }



}
