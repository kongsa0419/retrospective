package repository;

import domain.Hello;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelloMapper {
    List<Hello> getHellos();
}
