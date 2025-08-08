package dev.steelbookdb.steelbookapi.localization.language;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
