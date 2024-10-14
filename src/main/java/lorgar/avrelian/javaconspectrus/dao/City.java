package lorgar.avrelian.javaconspectrus.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

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
