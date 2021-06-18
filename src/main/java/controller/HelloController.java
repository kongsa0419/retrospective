package controller;

import domain.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import service.HelloService;

import java.util.List;

@Controller
public class HelloController {

    @Autowired
    private HelloService helloService;

    @ResponseBody
    @RequestMapping(value = "/hello")
    public String Hello(){
        return "Hello!";
    }

    @ResponseBody
    @RequestMapping(value="/hellos")
    public List<Hello> getHellos(){
        return helloService.getHellos();
    }
}
