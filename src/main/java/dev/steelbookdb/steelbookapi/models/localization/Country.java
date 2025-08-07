package dev.steelbookdb.steelbookapi.models.localization;

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
@Table(name = "countries")
public class Country extends BaseEntity {

    private String name;
}
