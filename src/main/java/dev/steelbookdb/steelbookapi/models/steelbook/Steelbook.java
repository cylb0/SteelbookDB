package dev.steelbookdb.steelbookapi.models.steelbook;

import java.time.LocalDate;

import dev.steelbookdb.steelbookapi.models.BaseEntity;
import jakarta.persistence.Entity;
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
@Table(name = "steelbooks")
public class Steelbook extends BaseEntity {

    private LocalDate releaseDate;
}
