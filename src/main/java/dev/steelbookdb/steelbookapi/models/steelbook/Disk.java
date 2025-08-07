package dev.steelbookdb.steelbookapi.models.steelbook;

import java.util.Set;

import dev.steelbookdb.steelbookapi.models.BaseEntity;
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
@Table(name = "disks")
public class Disk extends BaseEntity {
    
    private String format;
    private String region;

    @ManyToMany(mappedBy = "disks")
    private Set<Steelbook> steelbooks;
}
