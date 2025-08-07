package dev.steelbookdb.steelbookapi.models.steelbook;

import dev.steelbookdb.steelbookapi.models.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "audio_tracks")
public class AudioTrack extends BaseEntity {

}
