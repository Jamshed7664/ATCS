package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.LaneDirectionKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.lane.LaneDirection;

public interface LaneDirectionJpaDao extends JpaRepository<LaneDirection, LaneDirectionKey> {

	@Modifying
	@Query(value = "insert into lane_direction(lane_id, direction_id) values (?1,?2)", nativeQuery = true)
	public int save(String laneId, Long directionId);

	@Query(value = "select direction.id as id, direction.name as name, lane.id as laneId from LaneDirection laneDirection inner join Lane lane on laneDirection.lane.id = lane.id inner join Direction direction on laneDirection.direction.id = direction.id  where lane.location.id=?1")
	public List<Map<String,Object>> findDirectionByLocationId(String locationId);
	
	@Query(value = "select direction_id from lane_direction where lane_id=?1", nativeQuery = true)
	public List<Long> findAllDirectionIdsByLaneId(String laneId);

	@Modifying
	@Query(value = "delete from lane_direction where lane_id=?1 and direction_id in ?2", nativeQuery = true)
	public int deleteByLaneIdAndDirectionIds(String laneId, List<Long> directionIds);

}
