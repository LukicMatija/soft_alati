package rs.fon.bg.ac.rs.lukic_matija.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.fon.bg.ac.rs.lukic_matija.domain.City;

import java.util.UUID;

public interface CityRepository extends JpaRepository<City, Long> {

}
