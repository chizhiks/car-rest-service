package ua.foxminded.chyzhov.carrestservice.entity;

import jakarta.persistence.*;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cars")
public class Car {

    @Id
    @Column(name = "object_id", nullable = false, length = Integer.MAX_VALUE)
    private String objectId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @Column(name = "year", nullable = false)
    private Integer year;

    @ManyToMany
    @JoinTable(
            name = "cars_categories",
            joinColumns = @JoinColumn(name = "object_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

}