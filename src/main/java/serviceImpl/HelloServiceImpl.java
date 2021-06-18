package serviceImpl;

import domain.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.HelloMapper;
import service.HelloService;

import java.util.List;

@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    private HelloMapper helloMapper;

    @Override
    public List<Hello> getHellos() {
        return helloMapper.getHellos();
    }
}
