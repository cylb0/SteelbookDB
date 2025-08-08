package dev.steelbookdb.steelbookapi.steelbook.audiotrack;

import java.util.Set;

import dev.steelbookdb.steelbookapi.BaseEntity;
import dev.steelbookdb.steelbookapi.localization.language.Language;
import dev.steelbookdb.steelbookapi.steelbook.audioformat.AudioFormat;
import dev.steelbookdb.steelbookapi.steelbook.disk.Disk;
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
