package br.com.dbc.vemser.GymExploreAPI.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"gym", "user"})
@ToString(exclude = {"gym", "user"})
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    @SequenceGenerator(name = "review_seq", sequenceName = "seq_review", allocationSize = 1)
    @Column(name = "id_review")
    private Integer id;

    @Lob
    @Column(name = "comment_text")
    private String comment;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gym", nullable = false)
    @JsonBackReference("gym-review")
    private Gym gym;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)

    @JsonIgnore
    private UserEntity user;

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
    }
}