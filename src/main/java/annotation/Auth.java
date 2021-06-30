package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
Target, Retention, @interface 모두 어노테이션 클래스에 선언함
@Target : Auth라는 어노테이션이 어느 위치에 올지 정한다.(ElementType.TYPE 덕분에 class, interface, enum 등에서 사용가능, METHOD 덕분에 메소드 위에 올 수 있다.)
@Retention : 어노테이션의 유지기간 설정 (여기선 RetentionPolicy.RUNTIME으로 돼있으니까 런타임까지 @Auth가 유지됌. -> 코드에서 이 정보를 바탕으로 로직을 구현할 수 있다.)
@interface : 이 파일을 어노테이션 클래스로 선언하겠다는 의미
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    enum Role {ROOT, MANAGER, NORMAL}; // 시스템관리자, 개발팀원?(관리자), 일반유저
    enum Authority {RESERVATION, BLAME, NONE}; // 글쓴이: Reservation 수정가능, 신고자: BLAME 수정가능, NONE: 조회 및 신청만 가능

    Role role() default Role.NORMAL;
    Authority authority() default Authority.NONE;

}
