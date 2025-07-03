package br.com.dbc.vemser.GymExploreAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_purchases")
public class UserPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_purchase_seq")
    @SequenceGenerator(name = "user_purchase_seq", sequenceName = "seq_user_purchase", allocationSize = 1)
    @Column(name = "id_purchase")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item")
    private StoreItem item;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    public UserPurchase(UserEntity user, StoreItem item) {
        this.user = user;
        this.item = item;
        this.purchaseDate = LocalDateTime.now();
    }
}