package lorgar.avrelian.javaconspectrus.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class WhetherWind {
    private BigDecimal speed;
    private BigDecimal deg;
}
