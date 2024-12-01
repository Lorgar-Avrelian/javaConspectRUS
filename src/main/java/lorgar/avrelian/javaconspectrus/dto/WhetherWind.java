package lorgar.avrelian.javaconspectrus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhetherWind {
    private BigDecimal speed;
    private BigDecimal deg;
}
