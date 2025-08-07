package dev.steelbookdb.steelbookapi.models.movie;

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
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Table(name = "genres")
public class Genre extends BaseEntity {
    
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;
}
