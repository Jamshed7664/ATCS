package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.Equipment;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentResponse;

public interface EquipmentJpaDao extends JpaRepository<Equipment, String> {

	public Boolean existsByGroupIdAndOrganizationIdAndActive(String groupId, Long organizationId, Boolean active);

	public Boolean existsByEquipmentTypeIdAndOrganizationId(String equipmentTypeId, Long organizationId);
	
	public Boolean existsByEquipmentTypeIdAndOrganizationIdAndActive(String equipmentTypeId, Long organizationId,
			Boolean active);

	public long countByEquipmentTypeIdAndOrganizationIdAndActive(String equipmentTypeId, Long organizationId,
			Boolean active);

	public Boolean existsByIdAndOrganizationIdAndActive(String id, Long organizationId, Boolean active);

	public EquipmentResponse findResponseById(String id);

	public EquipmentResponse findByIdAndOrganizationIdAndActive(String id, Long organizationId, Boolean active);

	public List<EquipmentResponse> findByGroupIdAndOrganizationIdAndActive(String groupId, Long organizationId,
			Boolean active);

	public List<EquipmentResponse> findByGroupIdAndOrganizationIdAndActiveAndEquipmentMappingLocationIdAndEquipmentMappingArmIdNullAndEquipmentMappingLaneIdNull(
			String groupId, Long organizationId, Boolean active, String locationId);

	public List<EquipmentResponse> findByGroupIdAndOrganizationIdAndActiveAndEquipmentMappingLocationIdAndEquipmentMappingArmIdAndEquipmentMappingLaneIdNull(
			String groupId, Long organizationId, Boolean active, String locationId, String armId);

	public List<EquipmentResponse> findByGroupIdAndOrganizationIdAndActiveAndEquipmentMappingLocationIdAndEquipmentMappingArmIdAndEquipmentMappingLaneId(
			String groupId, Long organizationId, Boolean active, String locationId, String armId, String laneId);

	public List<EquipmentResponse> findByOrganizationIdAndActive(Long organizationId, Boolean active);

	@Query(value = "select equipment.id from Equipment equipment left join EquipmentMapping equipmentMapping on equipment.id = equipmentMapping.equipment.id where equipment.organization.id =?2 and equipment.active = ?3 and equipment.groupId = ?1 and equipmentMapping.lane.id is null and equipmentMapping.location.id is null and equipmentMapping.arm.id is null order by equipment.id asc")
	public List<String> findIdsByGroupIdAndOrganizationIdAndActiveAndLaneIdNullAndLocationIdNullAndArmIdNull(
			String groupId, Long organizationId, Boolean active);

	@Query(value = "select equipment.id from Equipment equipment inner join EquipmentMapping equipmentMapping on equipment.id = equipmentMapping.equipment.id where equipment.organization.id = :organizationId and equipment.active = :active and equipment.groupId = :groupId and equipmentMapping.location.id = :locationId")
	public List<String> findIdsByGroupIdAndOrganizationIdAndActiveAndLocationId(
			@Param(value = "groupId") String groupId, @Param(value = "organizationId") Long organizationId,
			@Param(value = "active") Boolean active, @Param(value = "locationId") String locationId);

	@Query(value = "select equipment.id from Equipment equipment left join EquipmentMapping equipmentMapping on equipment.id = equipmentMapping.equipment.id where equipment.organization.id =?2 and equipment.active = ?3 and equipment.groupId = ?1 and ((equipmentMapping.lane.id is null and equipmentMapping.location.id is null and equipmentMapping.arm.id is null) or (equipmentMapping.location.id = ?4))")
	public List<String> findIdsByGroupIdAndOrganizationIdAndActiveAndLaneIdNullAndLocationIdNullAndArmIdNullOrLocationId(
			String groupId, Long organizationId, Boolean active, String locationId);

	@Query(value = "select equipment.groupId as groupId, equipmentType.id as equipmentTypeId, equipmentType.code as equipmentTypeCode, equipmentType.name as equipmentTypeName, count(equipment.groupId) as count from Equipment equipment inner join EquipmentType equipmentType on equipment.equipmentType.id = equipmentType.id left join EquipmentMapping equipmentMapping on equipment.id= equipmentMapping.equipment.id where equipment.organization.id=?1 and equipment.active=?2 and equipmentMapping.lane.id is null and equipmentMapping.location.id is null and equipmentMapping.arm.id is null group by equipment.groupId, equipmentType.id order by equipment.groupId")
	public List<Map<String, Object>> findByOrganizationIdAndActiveAndLaneIdNullAndLocationIdNullAndArmIdNullAndGroupByGroupId(
			Long organizationId, Boolean active);

	@Query(value = "select equipment.groupId as groupId, equipmentType.id as equipmentTypeId, equipmentType.code as equipmentTypeCode, equipmentType.name as equipmentTypeName, count(equipment.groupId) as count from Equipment equipment inner join EquipmentType equipmentType on equipment.equipmentType.id = equipmentType.id left join EquipmentMapping equipmentMapping on equipment.id= equipmentMapping.equipment.id where equipmentMapping.location.id=?2 and equipment.active=?1 group by equipment.groupId, equipmentType.id order by equipment.groupId")
	public List<Map<String, Object>> findByActiveAndLocationIdAndGroupByGroupId(Boolean active, String locationId);

	@Query(value = "select equipment.groupId as groupId, count(equipment.groupId) as count from Equipment equipment left join EquipmentMapping equipmentMapping on equipment.id= equipmentMapping.equipment.id where equipment.organization.id=?1 and equipment.active=?2 and equipmentMapping.lane.id is null and equipmentMapping.location.id is null and equipmentMapping.arm.id is null group by equipment.groupId order by equipment.groupId")
	public List<Map<String, Object>> findGroupIdAndCountByOrganizationIdAndActiveAndLaneIdNullAndLocationIdNullAndArmIdNullAndGroupByGroupId(
			Long organizationId, Boolean active);

	@Query(value = "select count(equipment.groupId) from Equipment equipment left join EquipmentMapping equipmentMapping on equipment.id= equipmentMapping.equipment.id where equipment.groupId=?1 and equipment.active=?2 and equipmentMapping.lane.id is null and equipmentMapping.location.id is null and equipmentMapping.arm.id is null group by equipment.groupId")
	public Long findCountByGroupIdAndActiveAndLaneIdNullAndLocationIdNullAndArmIdNullAndGroupByGroupId(String groupId,
			Boolean active);

	@Query(value = "select id from Equipment where organization.id =?1 and active = ?2 and groupId = ?3")
	public List<String> findIdByOrganizationIdAndActiveAndGroupId(Long organizationId, Boolean active, String groupId);

	@Query(value = "select equipment.groupId as groupId, equipmentType.id as equipmentTypeId, equipmentType.code as equipmentTypeCode, equipmentType.name as equipmentTypeName, count(equipment.groupId) as count from Equipment equipment inner join EquipmentType equipmentType on equipment.equipmentType.id = equipmentType.id where equipment.organization.id=?1 and equipment.active = ?2 group by equipment.groupId,equipmentType order by equipmentType.id asc")
	public List<Map<String, Object>> findByOrganizationIdAndActiveAndGroupByGroupId(Long organizationId,
			Boolean active);

	@Query(value = "select equipment.equipmentType.id as equipmentTypeId, count(equipment.groupId) as count from Equipment equipment where equipment.organization.id=?1 and equipment.active = ?2 group by equipment.equipmentType.id order by equipment.equipmentType.id asc")
	public List<Map<String, Object>> findEquipmentTypeIdAndCountByOrganizationIdAndActiveAndGroupByEquipmentTypeId(
			Long organizationId, Boolean active);

	@Query(value = "select equipment.groupId as groupId, count(equipment.groupId) as count from Equipment equipment where equipment.organization.id=?1 and equipment.active = ?2 and equipment.equipmentType.id=?3 group by equipment.groupId")
	public List<Map<String, Object>> findByOrganizationIdAndActiveAndEquipmentTypeIdAndGroupByGroupId(
			Long organizationId, Boolean active, String equipmentTypeId);

	@Query(value = "select equipment.groupId as groupId, equipmentType.id as equipmentTypeId, equipmentType.code as equipmentTypeCode, equipmentType.name as equipmentTypeName, count(equipment.groupId) as count from Equipment equipment inner join EquipmentType equipmentType on equipment.equipmentType.id = equipmentType.id where equipment.organization.id=?1 and equipment.active = ?2 and equipment.groupId = ?3 group by equipment.groupId,equipmentType order by equipmentType.id asc")
	public Map<String, Object> findByOrganizationIdAndActiveAndGroupIdAndGroupByGroupId(Long organizationId,
			Boolean active, String groupId);

	@Query(value = "select equipment.groupId as groupId, equipmentType.id as equipmentTypeId, equipmentType.code as equipmentTypeCode, equipmentType.name as equipmentTypeName, count(group_id) as count from Equipment equipment inner join EquipmentType equipmentType on equipment.equipmentType.id = equipmentType.id where equipment.organization.id=?1 and equipment.groupId=?2 and equipment.active = ?3 and equipmentType.id=?4 group by group_id order by equipmentType.id asc")
	public Map<String, Object> findByOrganizationIdAndGroupIdAndActiveAndEquipmentTypeIdAndGroupByGroupId(
			Long organizationId, String groupId, Boolean active, String equipmentTypeId);

	@Query(value = "select equipment.groupId as groupId, equipmentType.id as equipmentTypeId, equipmentType.code as equipmentTypeCode, equipmentType.name as equipmentTypeName, count(group_id) as count from Equipment equipment inner join EquipmentType equipmentType on equipment.equipmentType.id = equipmentType.id where equipment.organization.id=?1 and equipment.groupId=?2 and equipment.active = ?3 group by group_id, equipmentType order by equipmentType.id asc")
	public Map<String, Object> findByOrganizationIdAndGroupIdAndActiveAndGroupByGroupId(Long organizationId,
			String groupId, Boolean active);

	@Modifying
	@Query(value = "update Equipment set active =false, modified_by =?2, modified_at =?3 where id in ?1")
	public int delete(List<String> equipmentIds, Long userId, Date date);

	@Modifying
	@Query(value = "update Equipment set active =false, modified_by =?3, modified_at =?4 where groupId = ?1 and organization.id = ?2")
	public int delete(String groupId, Long organizationId, Long userId, Date date);
}
