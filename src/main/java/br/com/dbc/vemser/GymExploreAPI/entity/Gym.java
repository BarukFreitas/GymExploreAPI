package br.com.dbc.vemser.GymExploreAPI.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "gyms")
@Data
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Lob
    @Column(name = "image_data", columnDefinition="BLOB")
    private byte[] imageData;

    @Column(name = "image_mime_type")
    private String imageMimeType;
}