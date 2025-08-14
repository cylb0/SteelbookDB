package dev.steelbookdb.steelbookapi.movie.director;

import java.util.Set;

import dev.steelbookdb.steelbookapi.BaseEntity;
import dev.steelbookdb.steelbookapi.localization.country.Country;
import dev.steelbookdb.steelbookapi.movie.movie.Movie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "directors")
@SuperBuilder
public class Director extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "director")
    private Set<Movie> movies;

    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id")
    private Country country;
}
