package br.com.dbc.vemser.GymExploreAPI.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    @SequenceGenerator(name = "post_seq", sequenceName = "seq_post", allocationSize = 1)
    @Column(name = "id_post")
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity user;

    public PostEntity(String content, UserEntity user) {
        this.content = content;
        this.user = user;
        this.timestamp = LocalDateTime.now();
    }

    public PostEntity(String content, String imageUrl, UserEntity user) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.user = user;
        this.timestamp = LocalDateTime.now();
    }
}