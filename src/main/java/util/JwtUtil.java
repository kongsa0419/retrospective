package util;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//다 손 봐줘야함
@Component()
@PropertySource("classpath:application.properties")
public class JwtUtil {

    @Value("${access.token.key}")
    String secret;

    /** 유저의 id값을 payload에 담아 만들어진 토큰을 통해 유저를 인증합니다 */
    public String genJsonWebToken(long id){

        Map<String, Object> headers = new HashMap<String, Object>(); // header
        headers.put("typ", "JWT");
        headers.put("alg","HS256");
        Map<String, Object> payloads = new HashMap<String, Object>(); //payload
        payloads.put("id", id );
        Calendar calendar = Calendar.getInstance(); // singleton object java calendar
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 24); // access token expire 24h later
        Date exp = calendar.getTime();

        return Jwts.builder().setHeader(headers).setClaims(payloads).setExpiration(exp).signWith(SignatureAlgorithm.HS256, secret.getBytes()).compact();
    }

    /** -1 : jwt가 null이거나 Bearer로 시작 x
     *  -2 : jwt 만료기간 넘음
     *  -3 : signatureException
     *  id : 정상작동 */
    // jwt의 payload에 더 많은 값을 넣게되면 Map<String, Object>식으로 변환
    public long getParsedIdIfJwtValid(String token) {
        try{
            if ( token == null)
                throw new IllegalAccessException("JWT: null임"); // 여러분들만의 Exception 객체를 만들어 계층구조를 만들어보는 것도 좋은 경험일 것 같습니다.
            else if ( !token.startsWith("Bearer ") )
                throw new IllegalAccessException("JWT: Bearer 로 시작안함");
        }catch (IllegalAccessException exception){
            exception.printStackTrace();
            return -1;
        }

        token = token.substring(7); // "Bearer " 제거

//        String[] jwtPart = token.split("\\.");
//
//        if(jwtPart.length !=3 )
//            throw new MalformedJwtException("\'~.~.~\' 형식이 아님. JWT 타입이 잘못됐음");
//        Map<String, Object> payloads  = null;
//        try {
//            payloads  = new ObjectMapper().readValue(new String(Base64.decodeBase64(jwtPart[1])), Map.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return -4;
//        }


        try {
            Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
            long id = Integer.valueOf(claims.get("id", Integer.class));
            return id;
        }catch (ExpiredJwtException e1){ //시간 지나면 체크하는지?
            e1.printStackTrace();
            return -2;
        }catch(SignatureException e2){
            e2.printStackTrace();
            return -3;
        }
    }

    public void isJwtSignatureValid(String signature) {

    }

}
