package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.DirectionJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.direction.Direction;
import com.nxtlife.efkon.enforcementconfigurator.service.DirectionService;
import com.nxtlife.efkon.enforcementconfigurator.view.direction.DirectionResponse;

@Service("directionServiceImpl")
@Transactional
public class DirectionServiceImpl implements DirectionService {

	@Autowired
	private DirectionJpaDao directionJpaDao;

	@PostConstruct
	private void init() {
		List<Direction> directions = new ArrayList<>();
		if (!directionJpaDao.existsByName("Left")) {
			directions.add(new Direction("Left"));
		}
		if (!directionJpaDao.existsByName("Right")) {
			directions.add(new Direction("Right"));
		}
		if (!directionJpaDao.existsByName("Straight")) {
			directions.add(new Direction("Straight"));
		}
		directionJpaDao.saveAll(directions);
	}

	public List<DirectionResponse> findAll() {
		return directionJpaDao.findAll().stream().map(DirectionResponse::new).collect(Collectors.toList());
	}

}
