package br.com.dbc.vemser.GymExploreAPI.entity;

import lombok.Data;
import javax.persistence.*;

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
}