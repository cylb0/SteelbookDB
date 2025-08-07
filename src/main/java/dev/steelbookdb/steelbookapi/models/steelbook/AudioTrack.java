package dev.steelbookdb.steelbookapi.models.steelbook;

import java.util.Set;

import dev.steelbookdb.steelbookapi.models.BaseEntity;
import dev.steelbookdb.steelbookapi.models.localization.Language;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "audio_tracks")
public class AudioTrack extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "audio_format_id")
    private AudioFormat audioFormat;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToMany(mappedBy = "audioTracks")
    private Set<Disk> disks;
}
