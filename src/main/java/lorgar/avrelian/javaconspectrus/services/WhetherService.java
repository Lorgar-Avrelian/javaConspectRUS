package lorgar.avrelian.javaconspectrus.services;

import lorgar.avrelian.javaconspectrus.dao.City;
import lorgar.avrelian.javaconspectrus.dto.Whether;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface WhetherService {
    Collection<City> getCityInfo(String city);

    Collection<Whether> getWhether(String city, String country);
}