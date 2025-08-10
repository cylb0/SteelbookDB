package dev.steelbookdb.steelbookapi.localization.language;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
    Optional<Language> findByCode(String code);
    Optional<Language> findByName(String name);
}
