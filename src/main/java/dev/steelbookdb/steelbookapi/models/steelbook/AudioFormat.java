package dev.steelbookdb.steelbookapi.models.steelbook;

import dev.steelbookdb.steelbookapi.models.BaseEntity;
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
