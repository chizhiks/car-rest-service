package ua.foxminded.chyzhov.carrestservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "makes")
public class Make {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "make_id", nullable = false)
    private Integer makeId;

    @Column(name = "make", nullable = false, length = Integer.MAX_VALUE)
    private String make;

}