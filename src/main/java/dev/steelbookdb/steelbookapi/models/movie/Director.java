package dev.steelbookdb.steelbookapi.models.movie;

import java.util.Set;

import dev.steelbookdb.steelbookapi.models.BaseEntity;
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
@Table(name = "directors")
public class Director extends BaseEntity {

    private String name;

    @OneToMany(mappedBy = "director")
    private Set<Movie> movies;
}
