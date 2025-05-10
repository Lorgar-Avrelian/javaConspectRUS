package lorgar.avrelian.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO {
    private String name;
    private HashMap<String, String> local_names;
    private double lat;
    private double lon;
    private String country;
    private String state;
}
