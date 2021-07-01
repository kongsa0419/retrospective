package controller;


import annotation.Auth;
import domain.Reservation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.ReservationService;

import javax.annotation.Resource;
//예약잡기, 수정, 삭제, 조회(id별로, list로), 권한 위임


@Controller
@RequestMapping(value = "/reservation")
public class ReservationController {

    @Resource
    private ReservationService mReservationService;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity createReservation(@RequestBody Reservation rsv){
        return new ResponseEntity(mReservationService.createNewReservation(rsv), HttpStatus.OK);
    }

    @Auth
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getReservation(@PathVariable long id){
        return new ResponseEntity(mReservationService.getReservation(id),HttpStatus.OK);
    }


    @Auth
    @ResponseBody
    @RequestMapping(value = "/mylist", method = RequestMethod.GET)
    public ResponseEntity getListIHosted(){
        return new ResponseEntity(mReservationService.getListIHosted(), HttpStatus.OK);
    }


    /*
    patch : 일부만 교체,
    put : 싹다 갈아엎음. 전달 데이터가 null이면 null로 교체
    미숙할 경우에 대비하여 Patch로 쓰는게 낫겠다는 생각
     */
    @Auth
    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public ResponseEntity patchReservation(@RequestBody Reservation rsv){
        return new ResponseEntity(mReservationService.updateReservation(rsv), HttpStatus.OK);
    }

    @Auth
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteReservation(@PathVariable long id){
        return new ResponseEntity(mReservationService.deleteReservation(id),HttpStatus.OK);
    }

}
