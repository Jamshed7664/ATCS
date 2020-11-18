package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.VehicleTypeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.vehicle.type.VehicleType;
import com.nxtlife.efkon.enforcementconfigurator.service.VehicleTypeService;
import com.nxtlife.efkon.enforcementconfigurator.view.vehicle.type.VehicleTypeResponse;

@Service("vehicleTypeServiceImpl")
@Transactional
public class VehicleTypeServiceImpl implements VehicleTypeService {

	@Autowired
	private VehicleTypeJpaDao vehicleTypeJpaDao;

	@PostConstruct
	private void init() {
		List<VehicleType> vehicleTypes = new ArrayList<>();
		if (!vehicleTypeJpaDao.existsByName("LMV")) {
			vehicleTypes.add(new VehicleType("LMV"));
		}
		if (!vehicleTypeJpaDao.existsByName("LCV")) {
			vehicleTypes.add(new VehicleType("LCV"));
		}
		if (!vehicleTypeJpaDao.existsByName("Truck/Bus")) {
			vehicleTypes.add(new VehicleType("Truck/Bus"));
		}
		if (!vehicleTypeJpaDao.existsByName("OSV")) {
			vehicleTypes.add(new VehicleType("OSV"));
		}
		if (!vehicleTypeJpaDao.existsByName("Two Wheeler")) {
			vehicleTypes.add(new VehicleType("Two Wheeler"));
		}
		vehicleTypeJpaDao.saveAll(vehicleTypes);
	}

	@Override
	public List<VehicleTypeResponse> findAll() {
		return vehicleTypeJpaDao.findAll().stream().map(VehicleTypeResponse::new).collect(Collectors.toList());
	}
}
