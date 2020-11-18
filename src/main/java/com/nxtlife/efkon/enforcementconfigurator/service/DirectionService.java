package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.view.direction.DirectionResponse;

public interface DirectionService {

    /**
     * this method used to fetch all the directions
     *
     * @return list of {@link DirectionResponse}
     */
	public List<DirectionResponse> findAll();
}
