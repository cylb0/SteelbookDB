package dev.steelbookdb.steelbookapi.movie.movie;

import java.util.Set;

import dev.steelbookdb.steelbookapi.BaseEntity;
import dev.steelbookdb.steelbookapi.localization.language.Language;
import dev.steelbookdb.steelbookapi.movie.director.Director;
import dev.steelbookdb.steelbookapi.movie.genre.Genre;
import dev.steelbookdb.steelbookapi.movie.movietranslation.MovieTranslation;
import dev.steelbookdb.steelbookapi.steelbook.disk.Disk;
import dev.steelbookdb.steelbookapi.steelbook.steelbook.Steelbook;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@NoArgsConstructor
@Table(name = "movies")
@SuperBuilder
public class Movie extends BaseEntity {

    private String title;
    private int releaseYear;
    private int runtime;
    private String posterUrl;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    @ManyToOne
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    @OneToMany(mappedBy = "movie")
    private Set<MovieTranslation> movieTranslations;

    @ManyToMany
    @JoinTable(
        name = "movie_genres",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @ManyToMany(mappedBy = "movies")
    private Set<Steelbook> steelbooks;

    @OneToMany(mappedBy = "movie")
    private Set<Disk> disks;

}
