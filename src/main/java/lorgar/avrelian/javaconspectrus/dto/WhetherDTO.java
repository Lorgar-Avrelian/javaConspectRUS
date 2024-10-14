package lorgar.avrelian.javaconspectrus.dto;

import java.math.BigDecimal;
import java.util.Objects;

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
