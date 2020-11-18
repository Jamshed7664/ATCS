package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.direction.Direction;

public interface DirectionJpaDao extends JpaRepository<Direction, Long> {

	public Boolean existsByName(String name);

	@Query(value = "select id from Direction")
	public List<Long> findAllIds();

}
