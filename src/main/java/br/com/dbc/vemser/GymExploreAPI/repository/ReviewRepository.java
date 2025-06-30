package br.com.dbc.vemser.GymExploreAPI.repository;

import br.com.dbc.vemser.GymExploreAPI.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByGymId(Integer gymId);
}