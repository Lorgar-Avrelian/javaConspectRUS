package lorgar.avrelian.javaconspectrus.services;

import lorgar.avrelian.javaconspectrus.dao.City;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WhetherService {
    List<City> getCityInfo(String city);
}
