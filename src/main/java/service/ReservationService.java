package service;

import domain.Reservation;

import java.util.List;

/*
+ getListIHosted() : List<Reservation>
+ getReservation(@RP rsvId) : Reservation
+ createNewReservation () : String
+ updateReservation (@RB Reservation) : String
+ deleteReservation (@RP rsvId) : String

* */
public interface ReservationService {
//    예약잡기, 수정, 삭제, 조회(id별로, list로), 권한 위임
    List<Reservation> getListIHosted();
    Reservation getReservation(long rsvId);
    String createNewReservation(Reservation rsv);
    String updateReservation(Reservation rsv);
    String deleteReservation(long rsvId);
}
