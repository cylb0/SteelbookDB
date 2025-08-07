package dev.steelbookdb.steelbookapi.steelbook;

import java.util.Set;

import dev.steelbookdb.steelbookapi.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
@Table(name = "retailers")
public class Retailer extends BaseEntity {

    private String name;
    private String website;

    @ManyToMany(mappedBy = "retailers")
    private Set<Steelbook> steelbooks;
}
