package repository;

import domain.Reservation;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ReservationMapper {
    List<Reservation> getRsvListByHostId(long id);

    Reservation getReservationByRsvId(long rsvId);

    int createNewRsv(Reservation rsv);

    int updateRsv(Reservation rsv);

    int deleteRsvById(long rsvId);

    int getCountRepeatedTime(Timestamp start, Timestamp end);

}
