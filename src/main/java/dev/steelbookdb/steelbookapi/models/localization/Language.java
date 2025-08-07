package dev.steelbookdb.steelbookapi.models.localization;

import java.util.Set;

import dev.steelbookdb.steelbookapi.models.BaseEntity;
import dev.steelbookdb.steelbookapi.models.movie.MovieTranslation;
import dev.steelbookdb.steelbookapi.models.steelbook.Disk;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
@Table(name = "languages")
public class Language extends BaseEntity {

    private String code;
    private String name;

    @OneToMany(mappedBy = "language")
    private Set<MovieTranslation> movieTranslations;

    @ManyToMany(mappedBy = "subtitleLanguages")
    private Set<Disk> disksWithSubDisks;

}
