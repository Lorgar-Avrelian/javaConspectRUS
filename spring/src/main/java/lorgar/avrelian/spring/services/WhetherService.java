package lorgar.avrelian.spring.services;

import lorgar.avrelian.spring.dao.City;
import lorgar.avrelian.spring.dto.Whether;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface WhetherService {
    Collection<City> getCityInfo(String city);

    Collection<Whether> getWhether(String city, String country);
}