## Пример 1:

> [[_оглавление_]](../README.md/#38-spring-rest-clients)

> [**[3.8.1 RestTemplate]**](/conspect/3.md/#381-resttemplate)

- создание и конфигурирование bean-компонента _RestTemplate_:

```java

@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }
}
```

- создание _DTO_ для конвертации принимаемых и отправляемых данных:

```java
public class CityDTO {
    private String name;
    private HashMap<String, String> local_names;
    private double lat;
    private double lon;
    private String country;
    private String state;

    public CityDTO() {
    }

    public CityDTO(String name, HashMap<String, String> local_names, double lat, double lon, String country, String state) {
        this.name = name;
        this.local_names = local_names;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getLocal_names() {
        return local_names;
    }

    public void setLocal_names(HashMap<String, String> local_names) {
        this.local_names = local_names;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityDTO cityDTO = (CityDTO) o;
        return Double.compare(lat, cityDTO.lat) == 0 && Double.compare(lon, cityDTO.lon) == 0 && Objects.equals(name, cityDTO.name) && Objects.equals(local_names, cityDTO.local_names) && Objects.equals(country, cityDTO.country) && Objects.equals(state, cityDTO.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, local_names, lat, lon, country, state);
    }

    @Override
    public String toString() {
        return "CityDTO{" +
                "name='" + name + '\'' +
                ", local_names=" + local_names +
                ", lat=" + lat +
                ", lon=" + lon +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
```

```java

@Schema(title = "Погода", description = "Информация о погоде")
public class Whether {
    @Schema(title = "Температура, ºC", description = "Значение температуры, ºC", defaultValue = "0", minimum = "-273", maximum = "400")
    private BigDecimal temperature;
    @Schema(title = "Ощущается, ºC", description = "Ощущается как температура, ºC", defaultValue = "0", minimum = "-273", maximum = "400")
    private BigDecimal feels_like;
    @Schema(title = "Минимальная температура, ºC", description = "Минимальное зафиксированное значение температуры, ºC", defaultValue = "0", minimum = "-273", maximum = "400")
    private BigDecimal temp_min;
    @Schema(title = "Максимальная температура, ºC", description = "Максимальное зафиксированное значение температуры, ºC", defaultValue = "0", minimum = "-273", maximum = "400")
    private BigDecimal temp_max;
    @Schema(title = "Давление, мм рт. ст.", description = "Значение атмосферного давления, мм рт. ст.", defaultValue = "746", minimum = "0", maximum = "1000")
    private BigDecimal pressure;
    @Schema(title = "Влажность, %", description = "Значение влажности воздуха, %", defaultValue = "50", minimum = "0", maximum = "100")
    private BigDecimal humidity;
    @Schema(title = "Видимость, м", description = "Значение прямой видимости, м", defaultValue = "5000", minimum = "0", maximum = "10000")
    private BigDecimal visibility;
    @Schema(title = "Скорость ветра, м/с", description = "Значение скорости ветра, м/с", defaultValue = "3", minimum = "0", maximum = "100")
    private BigDecimal wind_speed;
    @Schema(title = "Направление ветра, º", description = "Значение направления ветра, º", defaultValue = "0", minimum = "0", maximum = "360")
    private BigDecimal wind_deg;
    @Schema(title = "Восход", description = "Время восхода солнца", defaultValue = "06:00")
    private Time sunrise;
    @Schema(title = "Закат", description = "Время заката солнца", defaultValue = "20:00")
    private Time sunset;

    public Whether() {
    }

    public Whether(BigDecimal temperature, BigDecimal feels_like, BigDecimal temp_min, BigDecimal temp_max, BigDecimal pressure, BigDecimal humidity, BigDecimal visibility, BigDecimal wind_speed, BigDecimal wind_deg, Time sunrise, Time sunset) {
        this.temperature = temperature;
        this.feels_like = feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.pressure = pressure;
        this.humidity = humidity;
        this.visibility = visibility;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(BigDecimal feels_like) {
        this.feels_like = feels_like;
    }

    public BigDecimal getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(BigDecimal temp_min) {
        this.temp_min = temp_min;
    }

    public BigDecimal getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(BigDecimal temp_max) {
        this.temp_max = temp_max;
    }

    public BigDecimal getPressure() {
        return pressure;
    }

    public void setPressure(BigDecimal pressure) {
        this.pressure = pressure;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    public BigDecimal getVisibility() {
        return visibility;
    }

    public void setVisibility(BigDecimal visibility) {
        this.visibility = visibility;
    }

    public BigDecimal getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(BigDecimal wind_speed) {
        this.wind_speed = wind_speed;
    }

    public BigDecimal getWind_deg() {
        return wind_deg;
    }

    public void setWind_deg(BigDecimal wind_deg) {
        this.wind_deg = wind_deg;
    }

    public Time getSunrise() {
        return sunrise;
    }

    public void setSunrise(Time sunrise) {
        this.sunrise = sunrise;
    }

    public Time getSunset() {
        return sunset;
    }

    public void setSunset(Time sunset) {
        this.sunset = sunset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Whether whether = (Whether) o;
        return Objects.equals(temperature, whether.temperature) && Objects.equals(feels_like, whether.feels_like) && Objects.equals(temp_min, whether.temp_min) && Objects.equals(temp_max, whether.temp_max) && Objects.equals(pressure, whether.pressure) && Objects.equals(humidity, whether.humidity) && Objects.equals(visibility, whether.visibility) && Objects.equals(wind_speed, whether.wind_speed) && Objects.equals(wind_deg, whether.wind_deg) && Objects.equals(sunrise, whether.sunrise) && Objects.equals(sunset, whether.sunset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, feels_like, temp_min, temp_max, pressure, humidity, visibility, wind_speed, wind_deg, sunrise, sunset);
    }

    @Override
    public String toString() {
        return "Whether{" +
                "temperature=" + temperature +
                ", feels_like=" + feels_like +
                ", temp_min=" + temp_min +
                ", temp_max=" + temp_max +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", visibility=" + visibility +
                ", wind_speed=" + wind_speed +
                ", wind_deg=" + wind_deg +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }
}
```

```java
public class WhetherDTO {
    private WhetherMain main;
    private BigDecimal visibility;
    private WhetherWind wind;
    private WhetherSunshine sys;

    public WhetherDTO() {
    }

    public WhetherDTO(WhetherMain main, BigDecimal visibility, WhetherWind wind, WhetherSunshine sys) {
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.sys = sys;
    }

    public WhetherMain getMain() {
        return main;
    }

    public void setMain(WhetherMain main) {
        this.main = main;
    }

    public BigDecimal getVisibility() {
        return visibility;
    }

    public void setVisibility(BigDecimal visibility) {
        this.visibility = visibility;
    }

    public WhetherWind getWind() {
        return wind;
    }

    public void setWind(WhetherWind wind) {
        this.wind = wind;
    }

    public WhetherSunshine getSys() {
        return sys;
    }

    public void setSys(WhetherSunshine sys) {
        this.sys = sys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhetherDTO that = (WhetherDTO) o;
        return Objects.equals(main, that.main) && Objects.equals(visibility, that.visibility) && Objects.equals(wind, that.wind) && Objects.equals(sys, that.sys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(main, visibility, wind, sys);
    }

    @Override
    public String toString() {
        return "WhetherDTO{" +
                "main=" + main +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", sys=" + sys +
                '}';
    }
}
```

```java
public class WhetherMain {
    private BigDecimal temp;
    private BigDecimal feels_like;
    private BigDecimal temp_min;
    private BigDecimal temp_max;
    private BigDecimal pressure;
    private BigDecimal humidity;

    public WhetherMain() {
    }

    public WhetherMain(BigDecimal temp, BigDecimal feels_like, BigDecimal temp_min, BigDecimal temp_max, BigDecimal pressure, BigDecimal humidity) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public BigDecimal getTemp() {
        return temp;
    }

    public void setTemp(BigDecimal temp) {
        this.temp = temp;
    }

    public BigDecimal getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(BigDecimal feels_like) {
        this.feels_like = feels_like;
    }

    public BigDecimal getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(BigDecimal temp_min) {
        this.temp_min = temp_min;
    }

    public BigDecimal getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(BigDecimal temp_max) {
        this.temp_max = temp_max;
    }

    public BigDecimal getPressure() {
        return pressure;
    }

    public void setPressure(BigDecimal pressure) {
        this.pressure = pressure;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhetherMain that = (WhetherMain) o;
        return Objects.equals(temp, that.temp) && Objects.equals(feels_like, that.feels_like) && Objects.equals(temp_min, that.temp_min) && Objects.equals(temp_max, that.temp_max) && Objects.equals(pressure, that.pressure) && Objects.equals(humidity, that.humidity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temp, feels_like, temp_min, temp_max, pressure, humidity);
    }

    @Override
    public String toString() {
        return "WhetherMain{" +
                "temp=" + temp +
                ", feels_like=" + feels_like +
                ", temp_min=" + temp_min +
                ", temp_max=" + temp_max +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                '}';
    }
}
```

```java
public class WhetherSunshine {
    private long sunrise;
    private long sunset;

    public WhetherSunshine() {
    }

    public WhetherSunshine(long sunrise, long sunset) {
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhetherSunshine that = (WhetherSunshine) o;
        return sunrise == that.sunrise && sunset == that.sunset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sunrise, sunset);
    }

    @Override
    public String toString() {
        return "WhetherSunshine{" +
                "sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }
}
```

```java
public class WhetherWind {
    private BigDecimal speed;
    private BigDecimal deg;

    public WhetherWind() {
    }

    public WhetherWind(BigDecimal speed, BigDecimal deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public BigDecimal getDeg() {
        return deg;
    }

    public void setDeg(BigDecimal deg) {
        this.deg = deg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhetherWind that = (WhetherWind) o;
        return Objects.equals(speed, that.speed) && Objects.equals(deg, that.deg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speed, deg);
    }

    @Override
    public String toString() {
        return "WhetherWind{" +
                "speed=" + speed +
                ", deg=" + deg +
                '}';
    }
}
```

- создание DAO для хранения данных в базе данных:

```java

@Schema(title = "Город", description = "Информация о городе")
@Entity
@Table(name = "city")
public class City {
    @Schema(title = "ID", description = "ID города", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private long id;
    @Schema(title = "Название", description = "Название города", defaultValue = "Москва", required = true, minLength = 2, maxLength = 30)
    @Column(name = "name", nullable = false, length = 30)
    private String name;
    @Schema(title = "Широта", description = "Географическая широта города", defaultValue = "55.7504461", required = true, minimum = "0", maximum = "180")
    @Column(name = "latitude", nullable = false)
    private BigDecimal lat;
    @Schema(title = "Долгота", description = "Географическая долгота города", defaultValue = "37.6174943", required = true, minimum = "0", maximum = "180")
    @Column(name = "longitude", nullable = false)
    private BigDecimal lon;
    @Schema(title = "Страна", description = "Код страны", defaultValue = "RU", required = true, minLength = 2, maxLength = 30)
    @Column(name = "country", nullable = false, length = 30)
    private String country;
    @Schema(title = "Регион", description = "Регион страны", defaultValue = "Moscow", required = true, minLength = 2, maxLength = 30)
    @Column(name = "state", nullable = false, length = 30)
    private String state;
    @Schema(title = "Варианты написания", description = "Варианты написания названия города на различных языках", defaultValue = "{\"feature_name\":\"Moscow\",\"no\":\"Moskva\",\"bi\":\"Moskow\",\"na\":\"Moscow\",\"io\":\"Moskva\",\"bs\":\"Moskva\",\"jv\":\"Moskwa\",\"el\":\"Μόσχα\",\"mg\":\"Moskva\",\"ja\":\"モスクワ\",\"su\":\"Moskwa\",\"eo\":\"Moskvo\",\"ab\":\"Москва\",\"co\":\"Moscù\",\"is\":\"Moskva\",\"az\":\"Moskva\",\"hr\":\"Moskva\",\"iu\":\"ᒨᔅᑯ\",\"sk\":\"Moskva\",\"hy\":\"Մոսկվա\",\"sl\":\"Moskva\",\"uk\":\"Москва\",\"an\":\"Moscú\",\"sm\":\"Moscow\",\"yi\":\"מאסקווע\",\"be\":\"Масква\",\"ie\":\"Moskwa\",\"ro\":\"Moscova\",\"tr\":\"Moskova\",\"tt\":\"Мәскәү\",\"sr\":\"Москва\",\"mr\":\"मॉस्को\",\"kk\":\"Мәскеу\",\"mn\":\"Москва\",\"ca\":\"Moscou\",\"zh\":\"莫斯科\",\"ce\":\"Москох\",\"es\":\"Moscú\",\"vo\":\"Moskva\",\"av\":\"Москва\",\"gd\":\"Moscobha\",\"dz\":\"མོསི་ཀོ\",\"yo\":\"Mọsko\",\"nn\":\"Moskva\",\"bo\":\"མོ་སི་ཁོ།\",\"cy\":\"Moscfa\",\"ka\":\"მოსკოვი\",\"ug\":\"Moskwa\",\"sc\":\"Mosca\",\"cs\":\"Moskva\",\"ss\":\"Moscow\",\"lg\":\"Moosko\",\"dv\":\"މޮސްކޯ\",\"se\":\"Moskva\",\"ascii\":\"Moscow\",\"gv\":\"Moscow\",\"fr\":\"Moscou\",\"mt\":\"Moska\",\"am\":\"ሞስኮ\",\"sh\":\"Moskva\",\"it\":\"Mosca\",\"br\":\"Moskov\",\"ko\":\"모스크바\",\"ur\":\"ماسکو\",\"kv\":\"Мӧскуа\",\"et\":\"Moskva\",\"fo\":\"Moskva\",\"zu\":\"IMoskwa\",\"gl\":\"Moscova - Москва\",\"hi\":\"मास्को\",\"sg\":\"Moscow\",\"ru\":\"Москва\",\"kw\":\"Moskva\",\"da\":\"Moskva\",\"ln\":\"Moskú\",\"th\":\"มอสโก\",\"bg\":\"Москва\",\"li\":\"Moskou\",\"ku\":\"Moskow\",\"de\":\"Moskau\",\"my\":\"မော်စကိုမြို့\",\"ky\":\"Москва\",\"wa\":\"Moscou\",\"ga\":\"Moscó\",\"ak\":\"Moscow\",\"fi\":\"Moskova\",\"sw\":\"Moscow\",\"fa\":\"مسکو\",\"id\":\"Moskwa\",\"ht\":\"Moskou\",\"mk\":\"Москва\",\"uz\":\"Moskva\",\"tl\":\"Moscow\",\"mi\":\"Mohikau\",\"so\":\"Moskow\",\"wo\":\"Mosku\",\"sq\":\"Moska\",\"nl\":\"Moskou\",\"cu\":\"Москъва\",\"ps\":\"مسکو\",\"tg\":\"Маскав\",\"kn\":\"ಮಾಸ್ಕೋ\",\"fy\":\"Moskou\",\"st\":\"Moscow\",\"qu\":\"Moskwa\",\"ml\":\"മോസ്കോ\",\"ta\":\"மாஸ்கோ\",\"he\":\"מוסקווה\",\"ay\":\"Mosku\",\"cv\":\"Мускав\",\"ch\":\"Moscow\",\"ms\":\"Moscow\",\"lv\":\"Maskava\",\"la\":\"Moscua\",\"af\":\"Moskou\",\"lt\":\"Maskva\",\"za\":\"Moscow\",\"kg\":\"Moskva\",\"kl\":\"Moskva\",\"gn\":\"Mosku\",\"pt\":\"Moscou\",\"ia\":\"Moscova\",\"os\":\"Мæскуы\",\"oc\":\"Moscòu\",\"vi\":\"Mát-xcơ-va\",\"te\":\"మాస్కో\",\"sv\":\"Moskva\",\"ar\":\"موسكو\",\"pl\":\"Moskwa\",\"tk\":\"Moskwa\",\"eu\":\"Mosku\",\"en\":\"Moscow\",\"ty\":\"Moscou\",\"hu\":\"Moszkva\",\"bn\":\"মস্কো\",\"ba\":\"Мәскәү\",\"nb\":\"Moskva\"}", required = true)
    @Column(name = "local_names", nullable = false, length = 30)
    private String localNames;

    public City() {
    }

    public City(long id, String name, BigDecimal lat, BigDecimal lon, String country, String state, String localNames) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
        this.state = state;
        this.localNames = localNames;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocalNames() {
        return localNames;
    }

    public void setLocalNames(String localNames) {
        this.localNames = localNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id == city.id && Objects.equals(name, city.name) && Objects.equals(lat, city.lat) && Objects.equals(lon, city.lon) && Objects.equals(country, city.country) && Objects.equals(state, city.state) && Objects.equals(localNames, city.localNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lat, lon, country, state, localNames);
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", localNames='" + localNames + '\'' +
                '}';
    }
}
```

- создание _JPA_-репозитория для взаимодействия с базой данных:

```java

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByLocalNamesContainsIgnoreCase(String localName);

    List<City> findByLocalNamesContainsIgnoreCaseAndCountryEqualsIgnoreCase(String localName, String country);
}
```

- создание таблицы в базе данных с помощью _Liquibase_:

```yaml
databaseChangeLog:
  - include:
      file: liquibase/scripts/conspectus.sql
```

```sql
-- liquibase formatted sql

-- changeset tokovenko:1
CREATE TABLE expense
(
    id       SERIAL NOT NULL,
    title    TEXT   NOT NULL,
    date     DATE   NOT NULL,
    category TEXT,
    amount   FLOAT  NOT NULL
);

-- changeset tokovenko:2
INSERT INTO expense (id, title, date, category, amount)
VALUES (1, 'Проезд в автобусе', '2021-01-30', 'Транспорт', 50),
       (2, 'Проезд в метро', '2021-01-30', 'Транспорт', 50),
       (3, 'Покупка книги', '2021-01-31', 'Прочие покупки', 300),
       (4, 'Покупка продуктов', '2021-01-31', 'Покупка продуктов', 450),
       (5, 'Поход в кино', '2021-02-01', 'Развлечения', 400),
       (6, 'Кофе', '2021-02-01', 'Еда вне дома', 150),
       (7, 'Покупка продуктов', '2021-02-02', 'Покупка продуктов', 600),
       (8, 'Поход в театр', '2021-02-14', 'Развлечения', 1000),
       (9, 'Цветы', '2021-02-14', null, 500);

-- changeset tokovenko:3
CREATE INDEX category_index ON expense (category);

-- changeset tokovenko:4
CREATE TABLE reader
(
    id              BIGSERIAL,
    name            VARCHAR(12) NOT NULL,
    second_name     VARCHAR(16) NOT NULL,
    surname         VARCHAR(30) NOT NULL,
    personal_number INTEGER     NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

-- changeset tokovenko:5
INSERT INTO reader (id, name, second_name, surname, personal_number)
VALUES (1, 'Иван', 'Иванович', 'Иванов', 10),
       (2, 'Пётр', 'Петрович', 'Петров', 11),
       (3, 'Фёдор', 'Фёдорович', 'Фёдоров', 18),
       (4, 'Сидор', 'Фёдорович', 'Фёдоров', 110);

-- changeset tokovenko:6
CREATE TABLE book
(
    id        BIGSERIAL,
    author    VARCHAR(30) NOT NULL,
    title     VARCHAR(30) NOT NULL,
    year      SMALLINT CHECK ( year > 1970 ),
    reader_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (reader_id) REFERENCES reader (id)
);

-- changeset tokovenko:7
CREATE TABLE book_cover
(
    id            BIGSERIAL,
    file_path     VARCHAR(255) NOT NULL,
    file_size     INTEGER      NOT NULL,
    image_preview oid          NOT NULL,
    media_type    VARCHAR(30)  NOT NULL,
    book_id       BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (book_id) REFERENCES book (id)
);

-- changeset tokovenko:8
INSERT INTO book (id, author, title, year, reader_id)
VALUES (1, 'Л.Н.Толстой', 'Война и мир', 1986, null),
       (2, 'Н.В.Гоголь', 'Мёртвые души', 1986, 2),
       (3, 'А.С.Пушкин', 'Евгений Онегин', 1986, 1),
       (4, 'Ф.М.Достоевский', 'Идиот', 1986, null),
       (5, 'Ф.М.Достоевский', 'Братья Карамазовы', 1986, 2),
       (6, 'Ф.М.Достоевский', 'Преступление и наказание', 1987, null),
       (7, 'Н.В.Гоголь', 'Ревизор', 1987, null),
       (8, 'Н.В.Гоголь', 'Вий', 1989, null),
       (9, 'Н.В.Гоголь', 'Тарас Бульба', 1989, null),
       (10, 'А.С.Пушкин', 'Борис Годунов', 1987, null),
       (11, 'А.С.Пушкин', 'Капитанская дочка', 1987, null),
       (12, 'А.С.Пушкин', 'Медный всадник', 1989, 1),
       (13, 'А.С.Пушкин', 'Пиковая дама', 1989, null);

-- changeset tokovenko:9
CREATE INDEX reader_index ON book (reader_id);

-- changeset tokovenko:10
CREATE INDEX book_index ON book_cover (book_id);

-- changeset tokovenko:11
CREATE TABLE city
(
    id          BIGSERIAL,
    name        VARCHAR(255)    NOT NULL,
    latitude    NUMERIC(16, 13) NOT NULL,
    longitude   NUMERIC(16, 13) NOT NULL,
    country     VARCHAR(30)     NOT NULL,
    state       VARCHAR(30)     NOT NULL,
    local_names TEXT            NOT NULL,
    PRIMARY KEY (id)
);
```

- создание сервиса:

```java

@Service
public interface WhetherService {
    Collection<City> getCityInfo(String city);

    Collection<Whether> getWhether(String city, String country);
}
```

- создание реализации сервиса:

```java

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
```

- создание контроллера:

```java

@RestController
@RequestMapping(path = "/whether")
@Tag(name = "Контроллер погоды", description = "Контроллер для получения прогнозов погоды")
public class WhetherController {
    private final WhetherService whetherService;

    public WhetherController(@Qualifier(value = "whetherServiceImpl") WhetherService whetherService) {
        this.whetherService = whetherService;
    }

    @GetMapping(path = "/city-info")                    // http://localhost:8080/whether/city-info
    @Operation(
            summary = "Гео",
            description = "Получить гео данные города",
            tags = "Погода",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = City.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<City>> getCityInfo(@RequestParam(name = "city") @Parameter(description = "Название города", required = true, schema = @Schema(implementation = String.class), example = "Москва") String city) {
        Collection<City> cities = whetherService.getCityInfo(city);
        if (cities != null) {
            return ResponseEntity.ok(cities);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping                                             // http://localhost:8080/whether
    @Operation(
            summary = "Погода",
            description = "Получить данные о погоде",
            tags = "Погода",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Whether.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<Whether>> getWhether(@RequestParam(name = "city") @Parameter(description = "Название города", required = true, schema = @Schema(implementation = String.class), example = "Москва") String city, @RequestParam(name = "country") @Parameter(description = "Код страны", required = true, schema = @Schema(implementation = String.class), example = "RU") String country) {
        Collection<Whether> whether = whetherService.getWhether(city, country);
        if (whether != null) {
            return ResponseEntity.ok(whether);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
```