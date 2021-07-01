package interceptor;

import annotation.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import service.UserService;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService mUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //handler : this.handler(controller)
        if (!(handler instanceof HandlerMethod)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth auth = handlerMethod.getMethod().getDeclaredAnnotation(Auth.class); // 메소드의 어노테이션

        String token = "";

        if (auth == null) { // auth annotation 이 없다면
            return true;
        } else { // auth annotation 이 있다면
            token = request.getHeader("Authorization");
            if ( jwtUtil.getParsedIdIfJwtValid(token)==mUserService.getLoginUserId()){ //error 난게 없으면
                return true;
            }
            else{
                throw new Exception("jwt: non valid"); // 작동x
            }
        }
    }

    // postHandle에서 header에 값을 싣어줄수있다.
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//    }

}
