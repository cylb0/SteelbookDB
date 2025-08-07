package dev.steelbookdb.steelbookapi.models.movie;

import java.util.Set;

import dev.steelbookdb.steelbookapi.models.BaseEntity;
import dev.steelbookdb.steelbookapi.models.steelbook.Disk;
import dev.steelbookdb.steelbookapi.models.steelbook.Steelbook;
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

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Table(name = "movies")
public class Movie extends BaseEntity {

    private String title;
    private int releaseYear;
    private int runtime;
    private String posterUrl;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

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
