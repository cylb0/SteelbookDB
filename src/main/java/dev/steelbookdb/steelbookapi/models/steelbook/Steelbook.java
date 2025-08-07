package dev.steelbookdb.steelbookapi.models.steelbook;

import java.time.LocalDate;
import java.util.Set;

import dev.steelbookdb.steelbookapi.models.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "editor_id")
    private Editor editor;
    
    @ManyToMany
    @JoinTable(
        name = "steelbook_retailers",
        joinColumns = @JoinColumn(name = "steelbook_id"),
        inverseJoinColumns = @JoinColumn(name = "retailer_id")
    )
    private Set<Retailer> retailers;

    @ManyToMany
    @JoinTable(
        name = "steelbook_disks",
        joinColumns = @JoinColumn(name = "steelbook_id"),
        inverseJoinColumns = @JoinColumn(name = "disk_id")
    )
    private Set<Disk> disks;
}
