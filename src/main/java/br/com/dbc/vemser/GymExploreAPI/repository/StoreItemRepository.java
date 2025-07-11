package br.com.dbc.vemser.GymExploreAPI.repository;

import br.com.dbc.vemser.GymExploreAPI.entity.StoreItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreItemRepository extends JpaRepository<StoreItem, Integer> {}