package dev.steelbookdb.steelbookapi.models.movie;

import dev.steelbookdb.steelbookapi.models.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

}
