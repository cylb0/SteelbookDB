package dev.steelbookdb.steelbookapi.movie.director;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    boolean existsByName(String name);
}
