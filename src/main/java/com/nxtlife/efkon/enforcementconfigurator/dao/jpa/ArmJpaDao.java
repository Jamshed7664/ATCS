package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.arm.Arm;
import com.nxtlife.efkon.enforcementconfigurator.view.arm.ArmResponse;

public interface ArmJpaDao extends JpaRepository<Arm, String> {

	public List<ArmResponse> findByLocationIdAndActive(String locationId, Boolean active);

	public Boolean existsByIdAndLocationIdAndActive(String id, String locationId, Boolean active);

	@Query(value = "select id from Arm where location.id=?1")
	public List<String> findAllIdsByLocationId(String locationId);

	@Query(value = "select id from Arm where location.id=?1 and active =?2")
	public Set<String> findIdsByLocationIdAndActive(String locationId, Boolean active);

	@Modifying
	@Query(value = "update Arm set direction= ?2, modified_by =?3, modified_at =?4 where id=?1")
	public int update(String armId, String direction, Long userId, Date date);
	
	@Modifying
	@Query(value = "update Arm set active= false, modified_by =?2, modified_at =?3 where location.id=?1")
	public int deleteByLocationId(String locationId, Long userId, Date date);

}
