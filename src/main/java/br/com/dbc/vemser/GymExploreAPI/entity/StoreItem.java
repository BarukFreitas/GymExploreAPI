package br.com.dbc.vemser.GymExploreAPI.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "store_items")
public class StoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_item_seq")
    @SequenceGenerator(name = "store_item_seq", sequenceName = "seq_store_item", allocationSize = 1)
    @Column(name = "id_item")
    private Integer id;

    @Column(name = "item_name")
    private String name;

    @Column(name = "item_description")
    private String description;

    @Column(name = "points_cost")
    private Integer pointsCost;
}