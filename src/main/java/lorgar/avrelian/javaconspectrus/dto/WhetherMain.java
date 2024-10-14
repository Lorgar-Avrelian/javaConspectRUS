package lorgar.avrelian.javaconspectrus.dto;

import java.math.BigDecimal;
import java.util.Objects;

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
