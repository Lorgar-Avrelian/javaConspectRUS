package lorgar.avrelian.javaconspectrus.dto;

import java.math.BigDecimal;
import java.util.Objects;

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
