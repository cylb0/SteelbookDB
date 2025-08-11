package dev.steelbookdb.steelbookapi.movie.genre;

import java.util.Set;

import dev.steelbookdb.steelbookapi.BaseEntity;
import dev.steelbookdb.steelbookapi.movie.movie.Movie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Table(name = "genres")
@SuperBuilder
public class Genre extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;
}
