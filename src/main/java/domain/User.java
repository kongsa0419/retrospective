package domain;

import enums.LoginType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

//엄밀히 말하면 domain보다는 단순데이터로 쓰이는거같다?
// @Getter, @Setter, @NonNull, @EqualsAndHashCode, @ToString 에 대한 걸 모두 해주는 Annotation
@Data
public class User {

    private int id;
    private String collegeid;
    private Timestamp regdate;
    private String name;
    private String tel1;
    private String tel2;
    private String tel3;
    private String residence;
    private byte logintype; //DB: tinyint <-> java : byte (purpose was to use enum)
    private String email;
    private String pw;
    private int promcount; //약속수
    private int breakcount; //철회수
    private int attendrate; //참석률
    private Timestamp outdt;

}
