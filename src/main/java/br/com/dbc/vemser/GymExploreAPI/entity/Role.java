package br.com.dbc.vemser.GymExploreAPI.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "seq_role", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @Override
    public String getAuthority() {
        return roleName;
    }
}