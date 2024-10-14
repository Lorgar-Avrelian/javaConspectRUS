package lorgar.avrelian.javaconspectrus.dto;

import java.util.HashMap;
import java.util.Objects;

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
