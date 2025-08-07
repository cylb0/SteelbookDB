package dev.steelbookdb.steelbookapi.models;

import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@MappedSuperclass
@NoArgsConstructor
public class BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
