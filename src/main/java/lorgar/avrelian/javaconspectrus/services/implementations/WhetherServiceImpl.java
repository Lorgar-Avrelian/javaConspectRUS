package lorgar.avrelian.javaconspectrus.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lorgar.avrelian.javaconspectrus.dao.City;
import lorgar.avrelian.javaconspectrus.dto.CityDTO;
import lorgar.avrelian.javaconspectrus.repository.CityRepository;
import lorgar.avrelian.javaconspectrus.services.WhetherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service
public class WhetherServiceImpl implements WhetherService {
    @Value("${whether.api.key}")
    private String apiKey;
    @Value("${whether.geo.url}")
    private String geoUrl;
    @Value("${whether.geo.result.count}")
    private int resultCount;
    @Autowired
    private RestTemplate restTemplate;
    private ObjectMapper mapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(WhetherServiceImpl.class);
    private final CityRepository cityRepository;

    public WhetherServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> getCityInfo(String city) {
        String uri = geoUrl + "?q=" + city + "&limit=" + resultCount + "&appid=" + apiKey;
        List<City> citiesList = cityRepository.findByLocalNamesContainsIgnoreCase(city);
        if (citiesList.isEmpty()) {
            CityDTO[] cities = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity(HttpHeaders.EMPTY), CityDTO[].class).getBody();
            CityDTO cityDTO;
            HashMap<String, String> localNames = new HashMap<>();
            if (cities.length == 0) {
                return null;
            } else {
                for (int i = 0; i < cities.length; i++) {
                    cityDTO = cities[i];
                    City cityDAO = new City();
                    cityDAO.setCountry(cityDTO.getCountry());
                    cityDAO.setLat(BigDecimal.valueOf(cityDTO.getLat()));
                    cityDAO.setLon(BigDecimal.valueOf(cityDTO.getLon()));
                    cityDAO.setName(cityDTO.getName());
                    if (cityDTO.getState() != null) {
                        cityDAO.setState(cityDTO.getState());
                    } else {
                        cityDAO.setState("N/A");
                    }
                    if (localNames.isEmpty()) {
                        localNames = cityDTO.getLocal_names();
                    }
                    if (cityDTO.getLocal_names() == null || cityDTO.getLocal_names().isEmpty()) {
                        cityDTO.setLocal_names(localNames);
                    }
                    if (cityDTO.getLocal_names().containsValue(localNames.get("en")) ||
                            cityDTO.getLocal_names().containsValue(localNames.get("es")) ||
                            cityDTO.getLocal_names().containsValue(localNames.get("fr")) ||
                            cityDTO.getLocal_names().containsValue(localNames.get("it")) ||
                            cityDTO.getLocal_names().containsValue(localNames.get("de")) ||
                            cityDTO.getLocal_names().containsValue(localNames.get("ru")) ||
                            cityDTO.getLocal_names().containsValue(city)) {
                        localNames.putAll(cityDTO.getLocal_names());
                        try {
                            cityDAO.setLocalNames(mapper.writeValueAsString(localNames));
                        } catch (JsonProcessingException e) {
                            logger.warn("Converting value error: " + e.getMessage());
                            continue;
                        }
                    } else {
                        try {
                            cityDAO.setLocalNames(mapper.writeValueAsString(cityDTO.getLocal_names()));
                        } catch (JsonProcessingException e) {
                            logger.warn("Converting value error: " + e.getMessage());
                            continue;
                        }
                    }
                    cityRepository.save(cityDAO);
                }
                return cityRepository.findByLocalNamesContainsIgnoreCase(city);
            }
        } else {
            return citiesList;
        }
    }
}
