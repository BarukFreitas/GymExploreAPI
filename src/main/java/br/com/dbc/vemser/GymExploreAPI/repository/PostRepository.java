package br.com.dbc.vemser.GymExploreAPI.repository;

import br.com.dbc.vemser.GymExploreAPI.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findByUser_Id(Long userId);

    List<PostEntity> findAllByOrderByTimestampDesc();
}