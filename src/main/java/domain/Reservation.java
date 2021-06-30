package domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

//id, host, entire_guest_count,  progression, createdt, modifieddt, rsvstart, rsvend
@Data
public class Reservation {
    private long id; //예약id
    private long host; //주최 userId
    private String title; // 제목 not null
    private String description; // 내용 nullable
    private int entire_guest_count; /** GuestList랑 JOIN해서 count where(host = guestset.id) 해줄것임 */
    private short is_deleted; //진행상태 (Y/N)
    private Timestamp createdt;
    private Timestamp modifieddt;
    private Timestamp rsvstart; //NN
    private Timestamp rsvend; //NN

}
