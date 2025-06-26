package br.com.dbc.vemser.GymExploreAPI.repository;

import br.com.dbc.vemser.GymExploreAPI.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Integer> {
}