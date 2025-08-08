package dev.steelbookdb.steelbookapi.localization.country;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
}
