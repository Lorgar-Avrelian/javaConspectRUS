package lorgar.avrelian.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhetherDTO {
    private WhetherMain main;
    private BigDecimal visibility;
    private WhetherWind wind;
    private WhetherSunshine sys;
}
