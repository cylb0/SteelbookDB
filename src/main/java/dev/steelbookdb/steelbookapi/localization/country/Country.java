package dev.steelbookdb.steelbookapi.localization.country;

import java.util.Set;

import dev.steelbookdb.steelbookapi.BaseEntity;
import dev.steelbookdb.steelbookapi.steelbook.editor.Editor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "countries")
public class Country extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    public Country(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "country")
    private Set<Editor> editors;
}
