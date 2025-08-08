package dev.steelbookdb.steelbookapi.steelbook.audioformat;

import dev.steelbookdb.steelbookapi.BaseEntity;
import jakarta.persistence.Entity;
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
@Table(name = "audio_formats")
public class AudioFormat extends BaseEntity {

    private String name;
}
