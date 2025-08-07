package dev.steelbookdb.steelbookapi.steelbook;

import java.util.Set;

import dev.steelbookdb.steelbookapi.BaseEntity;
import dev.steelbookdb.steelbookapi.localization.Language;
import dev.steelbookdb.steelbookapi.movie.Movie;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "disks")
public class Disk extends BaseEntity {
    
    @Enumerated(EnumType.STRING)
    private DiskFormat format;
    private String region;

    @ManyToMany(mappedBy = "disks")
    private Set<Steelbook> steelbooks;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToMany
    @JoinTable(
        name = "disk_audiotracks",
        joinColumns = @JoinColumn(name = "disk_id"),
        inverseJoinColumns = @JoinColumn(name = "audio_track_id")
    )
    private Set<AudioTrack> audioTracks;

    @ManyToMany
    @JoinTable(
        name = "disk_subtitles",
        joinColumns = @JoinColumn(name = "disk_id"),
        inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<Language> subtitleLanguages;
}
