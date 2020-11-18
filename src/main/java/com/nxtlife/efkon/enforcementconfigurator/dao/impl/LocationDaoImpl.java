package com.nxtlife.efkon.enforcementconfigurator.dao.impl;

import org.springframework.stereotype.Repository;

import com.nxtlife.efkon.enforcementconfigurator.dao.LocationDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;

@Repository("locationDaoImpl")
public class LocationDaoImpl extends BaseDao<String, Location> implements LocationDao {

}
