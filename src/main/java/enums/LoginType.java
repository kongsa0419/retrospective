package enums;

import lombok.Getter;

@Getter
public enum LoginType {

    LEGACY("legacy login", 1),
    GOOGLE("google login",2);


    private String type;
    private int id;

    //default 접근자 :
    LoginType(String type, int id) {
        this.type = type;
        this.id = id;
    }

}