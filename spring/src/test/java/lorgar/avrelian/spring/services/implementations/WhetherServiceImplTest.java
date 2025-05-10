package lorgar.avrelian.spring.services.implementations;

import lorgar.avrelian.spring.dao.City;
import lorgar.avrelian.spring.dto.CityDTO;
import lorgar.avrelian.spring.dto.Whether;
import lorgar.avrelian.spring.dto.WhetherDTO;
import lorgar.avrelian.spring.repository.CityRepository;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import static lorgar.avrelian.spring.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
        Assertions.assertDoesNotThrow(() -> whetherService.getCityInfo(LONDON.getName()));
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
        String city = LONDON.getName();
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
        Assertions.assertDoesNotThrow(() -> whetherService.getCityInfo(LONDON.getName()));
        assertIterableEquals(CITIES, actualCities);
    }

    @Test
    @DisplayName(value = "getCityInfo(String city): return Collection<>")
    @Order(3)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getCityInfo3() {
        when(cityRepository.findByLocalNamesContainsIgnoreCase(anyString()))
                .thenReturn(new ArrayList<>());
        String city = LONDON.getName();
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
        Assertions.assertDoesNotThrow(() -> whetherService.getCityInfo(LONDON.getName()));
        assertIterableEquals(Collections.EMPTY_LIST, actualCities);
    }

    @Test
    @DisplayName(value = "getWhether(String city, String country): return Collection<Whether>")
    @Order(4)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getWhether1() {
        when(cityRepository.findByLocalNamesContainsIgnoreCase(anyString()))
                .thenReturn(CITIES);
        when(cityRepository.findByLocalNamesContainsIgnoreCaseAndCountryEqualsIgnoreCase(eq(MOSCOW.getName()), eq(MOSCOW.getCountry())))
                .thenReturn(new ArrayList<>(List.of(MOSCOW)));
        String uri = currentWhetherUrl + "?units=metric&lat=" + MOSCOW.getLat() + "&lon=" + MOSCOW.getLon() + "&appid=" + apiKey;
        when(restTemplate.getForObject(uri, WhetherDTO.class)).
                thenReturn(WHETHER_DTO);
        //
        Collection<Whether> actualWhether = whetherService.getWhether(MOSCOW.getName(), MOSCOW.getCountry());
        assertNotNull(actualWhether);
        assertInstanceOf(Collection.class, actualWhether);
        Assertions.assertDoesNotThrow(() -> whetherService.getWhether(MOSCOW.getName(), MOSCOW.getCountry()));
        assertIterableEquals(new ArrayList<>(List.of(WHETHER)), actualWhether);
    }

    @Test
    @DisplayName(value = "getWhether(String city, String country): return null")
    @Order(5)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getWhether2() {
        when(cityRepository.findByLocalNamesContainsIgnoreCase(anyString()))
                .thenReturn(new ArrayList<>());
        String city = LONDON.getName();
        String uri = geoUrl + "?q=" + city + "&limit=" + resultCount + "&appid=" + apiKey;
        ResponseEntity<CityDTO[]> responseEntity = new ResponseEntity<>(CITIES_DTO,
                                                                        new HttpHeaders(),
                                                                        HttpStatus.OK.value());
        when(restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(HttpHeaders.EMPTY), CityDTO[].class))
                .thenReturn(responseEntity);
        //
        Collection<Whether> actualWhether = whetherService.getWhether(LONDON.getName(), LONDON.getCountry());
        assertNull(actualWhether);
        Assertions.assertDoesNotThrow(() -> whetherService.getWhether(LONDON.getName(), LONDON.getCountry()));
    }

    @Test
    @DisplayName(value = "getWhether(String city, String country): return null")
    @Order(6)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getWhether3() {
        when(cityRepository.findByLocalNamesContainsIgnoreCase(anyString()))
                .thenReturn(new ArrayList<>())
                .thenReturn(CITIES);
        String city = NOGINSK.getName();
        String uri = geoUrl + "?q=" + city + "&limit=" + resultCount + "&appid=" + apiKey;
        ResponseEntity<CityDTO[]> responseEntity = new ResponseEntity<>(CITIES_DTO,
                                                                        new HttpHeaders(),
                                                                        HttpStatus.OK.value());
        when(restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(HttpHeaders.EMPTY), CityDTO[].class))
                .thenReturn(responseEntity);
        when(cityRepository.findByLocalNamesContainsIgnoreCaseAndCountryEqualsIgnoreCase(eq(NOGINSK.getName()), eq(NOGINSK.getCountry())))
                .thenReturn(new ArrayList<>());
        //
        Collection<Whether> actualWhether = whetherService.getWhether(NOGINSK.getName(), NOGINSK.getCountry());
        assertNull(actualWhether);
        Assertions.assertDoesNotThrow(() -> whetherService.getWhether(NOGINSK.getName(), NOGINSK.getCountry()));
    }
}