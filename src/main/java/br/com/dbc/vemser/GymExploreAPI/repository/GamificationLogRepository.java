package br.com.dbc.vemser.GymExploreAPI.repository;

import br.com.dbc.vemser.GymExploreAPI.entity.GamificationLog;
import br.com.dbc.vemser.GymExploreAPI.enums.PointAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface GamificationLogRepository extends JpaRepository<GamificationLog, Integer> {

    long countByUserIdAndActionAndActionDate(Long userId, PointAction action, LocalDate date);

}