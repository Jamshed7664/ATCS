package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.blinking.time.slots.BlinkingTimeSlot;
import com.nxtlife.efkon.enforcementconfigurator.view.blinking.time.slots.BlinkingTimeSlotResponse;

public interface BlinkingTimeSlotJpaDao extends JpaRepository<BlinkingTimeSlot, Long> {

	List<BlinkingTimeSlotResponse> findByCameraIdAndActive(String cameraId, Boolean active);

	@Query(value = "select id from BlinkingTimeSlot where camera.id=?1 and active=?2")
	public Set<Long> findIdsByCameraIdAndActive(String cameraId, Boolean active);

	@Modifying
	@Query(value = "update BlinkingTimeSlot set startTime=?1,endTime=?2,modified_by=?4,modified_at=?5 where id=?3")
	public int update(LocalTime startTime, LocalTime endTime, Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update BlinkingTimeSlot set active = false, modified_by =?2, modified_at =?3 where id in ?1")
	public int deleteByIds(Set<Long> ids, Long userId, Date date);
}
