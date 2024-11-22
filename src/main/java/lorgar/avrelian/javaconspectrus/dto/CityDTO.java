package lorgar.avrelian.javaconspectrus.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
@Data
public class CityDTO {
    private String name;
    private HashMap<String, String> local_names;
    private double lat;
    private double lon;
    private String country;
    private String state;
}
