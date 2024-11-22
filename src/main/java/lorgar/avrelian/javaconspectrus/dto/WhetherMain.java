package lorgar.avrelian.javaconspectrus.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class WhetherMain {
    private BigDecimal temp;
    private BigDecimal feels_like;
    private BigDecimal temp_min;
    private BigDecimal temp_max;
    private BigDecimal pressure;
    private BigDecimal humidity;
}
