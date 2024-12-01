package lorgar.avrelian.javaconspectrus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;

@Schema(title = "Погода", description = "Информация о погоде")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
