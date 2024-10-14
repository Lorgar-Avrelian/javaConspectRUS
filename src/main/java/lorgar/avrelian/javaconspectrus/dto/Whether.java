package lorgar.avrelian.javaconspectrus.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Objects;

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
