package br.com.dbc.vemser.GymExploreAPI.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gyms")
@Data
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gym_seq")
    @SequenceGenerator(name = "gym_seq", sequenceName = "seq_gym", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("gym-review")
    private Set<Review> reviews = new HashSet<>();
}