package lorgar.avrelian.javaconspectrus.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lorgar.avrelian.javaconspectrus.dao.City;
import lorgar.avrelian.javaconspectrus.dto.CityDTO;
import lorgar.avrelian.javaconspectrus.dto.Whether;
import lorgar.avrelian.javaconspectrus.dto.WhetherDTO;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public class WhetherServiceImpl implements WhetherService {
    @Value("${whether.api.key}")
    private String apiKey;
    @Value("${whether.geo.url}")
    private String geoUrl;
    @Value("${whether.current.url}")
    private String currentWhetherUrl;
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
    public Collection<City> getCityInfo(String city) {
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

    @Override
    public Collection<Whether> getWhether(String city, String country) {
        Collection<City> cities = getCityInfo(city);
        if (cities.isEmpty()) {
            return null;
        }
        cities = cityRepository.findByLocalNamesContainsIgnoreCaseAndCountryEqualsIgnoreCase(city, country);
        if (cities.isEmpty()) {
            return null;
        }
        BigDecimal lat;
        BigDecimal lon;
        List<Whether> whethers = new ArrayList<>();
        Whether whether = new Whether();
        for (City city1 : cities) {
            lat = city1.getLat();
            lon = city1.getLon();
            String uri = currentWhetherUrl + "?units=metric&lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;
            WhetherDTO whetherDTO = restTemplate.getForObject(uri, WhetherDTO.class);
            if (whetherDTO != null) {
                whether.setTemperature(whetherDTO.getMain().getTemp());
                whether.setFeels_like(whetherDTO.getMain().getFeels_like());
                whether.setTemp_min(whetherDTO.getMain().getTemp_min());
                whether.setTemp_max(whetherDTO.getMain().getTemp_max());
                whether.setPressure(whetherDTO.getMain().getPressure().multiply(BigDecimal.valueOf(0.7500615758456601)));
                whether.setHumidity(whetherDTO.getMain().getHumidity());
                whether.setVisibility(whetherDTO.getVisibility());
                whether.setWind_speed(whetherDTO.getWind().getSpeed());
                whether.setWind_deg(whetherDTO.getWind().getDeg());
                whether.setSunrise(new Time(whetherDTO.getSys().getSunrise() * 1000));
                whether.setSunset(new Time(whetherDTO.getSys().getSunset() * 1000));
                whethers.add(whether);
            }
        }
        return whethers;
    }
}