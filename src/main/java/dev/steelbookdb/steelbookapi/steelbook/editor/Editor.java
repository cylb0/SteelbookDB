package dev.steelbookdb.steelbookapi.steelbook.editor;

import java.util.Set;

import dev.steelbookdb.steelbookapi.BaseEntity;
import dev.steelbookdb.steelbookapi.localization.country.Country;
import dev.steelbookdb.steelbookapi.steelbook.steelbook.Steelbook;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "editors")
public class Editor extends BaseEntity {

    private String name;
    private String website;
    
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "editor")
    private Set<Steelbook> steelbooks;
}
