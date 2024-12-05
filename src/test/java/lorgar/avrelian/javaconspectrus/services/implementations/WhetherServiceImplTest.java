package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.dao.City;
import lorgar.avrelian.javaconspectrus.dto.CityDTO;
import lorgar.avrelian.javaconspectrus.repository.CityRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static lorgar.avrelian.javaconspectrus.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName(value = "Test of WhetherServiceImpl")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WhetherServiceImplTest {
    @Value("${whether.api.key}")
    private String apiKey;
    @Value("${whether.geo.url}")
    private String geoUrl;
    @Value("${whether.current.url}")
    private String currentWhetherUrl;
    @Value("${whether.geo.result.count}")
    private int resultCount;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private WhetherServiceImpl whetherService;

    @Test
    @DisplayName(value = "getCityInfo(String city): return Collection<City>")
    @Order(1)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getCityInfo1() {
        when(cityRepository.findByLocalNamesContainsIgnoreCase(anyString()))
                .thenReturn(CITIES);
        //
        Collection<City> actualCities = whetherService.getCityInfo(LONDON.getName());
        assertNotNull(actualCities);
        assertInstanceOf(Collection.class, actualCities);
        assertDoesNotThrow(() -> whetherService.getCityInfo(LONDON.getName()));
        assertIterableEquals(CITIES, actualCities);
    }

    @Test
    @DisplayName(value = "getCityInfo(String city): return new Collection<City>")
    @Order(2)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getCityInfo2() {
        when(cityRepository.findByLocalNamesContainsIgnoreCase(anyString()))
                .thenReturn(new ArrayList<>())
                .thenReturn(CITIES);
        String city = "London";
        String uri = geoUrl + "?q=" + city + "&limit=" + resultCount + "&appid=" + apiKey;
        ResponseEntity<CityDTO[]> responseEntity = new ResponseEntity<>(CITIES_DTO,
                                                                        new HttpHeaders(),
                                                                        HttpStatus.OK.value());
        when(restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(HttpHeaders.EMPTY), CityDTO[].class))
                .thenReturn(responseEntity);
        //
        Collection<City> actualCities = whetherService.getCityInfo(LONDON.getName());
        assertNotNull(actualCities);
        assertInstanceOf(Collection.class, actualCities);
        assertDoesNotThrow(() -> whetherService.getCityInfo(LONDON.getName()));
        assertIterableEquals(CITIES, actualCities);
    }

    @Test
    @DisplayName(value = "getCityInfo(String city): return Collection<>")
    @Order(3)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getCityInfo3() {
        when(cityRepository.findByLocalNamesContainsIgnoreCase(anyString()))
                .thenReturn(new ArrayList<>());
        String city = "London";
        String uri = geoUrl + "?q=" + city + "&limit=" + resultCount + "&appid=" + apiKey;
        ResponseEntity<CityDTO[]> responseEntity = new ResponseEntity<>(CITIES_DTO,
                                                                        new HttpHeaders(),
                                                                        HttpStatus.OK.value());
        when(restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(HttpHeaders.EMPTY), CityDTO[].class))
                .thenReturn(responseEntity);
        //
        Collection<City> actualCities = whetherService.getCityInfo(LONDON.getName());
        assertNotNull(actualCities);
        assertInstanceOf(Collection.class, actualCities);
        assertDoesNotThrow(() -> whetherService.getCityInfo(LONDON.getName()));
        assertIterableEquals(Collections.EMPTY_LIST, actualCities);
    }
}