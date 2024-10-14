package lorgar.avrelian.javaconspectrus.repository;

import lorgar.avrelian.javaconspectrus.dao.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByLocalNamesContainsIgnoreCase(String localName);
}
