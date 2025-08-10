package dev.steelbookdb.steelbookapi.localization.language;

import java.util.Set;

import dev.steelbookdb.steelbookapi.BaseEntity;
import dev.steelbookdb.steelbookapi.movie.movietranslation.MovieTranslation;
import dev.steelbookdb.steelbookapi.steelbook.audiotrack.AudioTrack;
import dev.steelbookdb.steelbookapi.steelbook.disk.Disk;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
@Table(name = "languages")
@SuperBuilder
public class Language extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "language")
    private Set<MovieTranslation> movieTranslations;

    @ManyToMany(mappedBy = "subtitleLanguages")
    private Set<Disk> disksWithSubDisks;
    
    @OneToMany(mappedBy = "language")
    private Set<AudioTrack> audioTracks;

}
