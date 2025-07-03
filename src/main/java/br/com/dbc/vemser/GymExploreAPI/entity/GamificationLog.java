package br.com.dbc.vemser.GymExploreAPI.entity;

import br.com.dbc.vemser.GymExploreAPI.enums.PointAction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "GamificationLog")
@Table(name = "gamification_log")
public class GamificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gamification_log_seq")
    @SequenceGenerator(name = "gamification_log_seq", sequenceName = "seq_gamification_log", allocationSize = 1)
    @Column(name = "id_gamification_log")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private PointAction action;

    @Column(name = "action_date")
    private LocalDate actionDate;

    public GamificationLog(UserEntity user, PointAction action) {
        this.user = user;
        this.action = action;
        this.actionDate = LocalDate.now();
    }
}