package lorgar.avrelian.javaconspectrus.dto;

import java.util.Objects;

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
