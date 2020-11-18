package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.EquipmentMapping;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentMappingResponse;

public interface EquipmentMappingJpaDao extends JpaRepository<EquipmentMapping, String> {

	public Boolean existsByEquipmentGroupIdAndEquipmentActive(String groupId, Boolean active);

	public Boolean existsByLocationIdAndActive(String id, Boolean active);

	public EquipmentMappingResponse findByEquipmentIdAndActive(String equipmentId, Boolean active);

	public List<EquipmentMappingResponse> findByActiveAndLocationId(Boolean active, String locationId);

	public Boolean existsByLocationIdAndEquipmentIdAndActive(String locationId, String equipmentId, Boolean active);

	@Query("select equipment.groupId as groupId, count(distinct equipment.id) as count from EquipmentMapping equipmentMapping inner join Equipment equipment on equipmentMapping.equipment.id = equipment.id where equipment.active=?1 and equipmentMapping.active=?1 group by equipment.groupId")
	public List<Map<String, Object>> findEquipmentGroupIdAndCountByActive(Boolean active);

	@Query("select equipment.equipmentType.id as equipmentTypeId, count(distinct equipment.id) as count from EquipmentMapping equipmentMapping inner join Equipment equipment on equipmentMapping.equipment.id = equipment.id where equipment.active=?1 and equipmentMapping.active=?1 group by equipment.equipmentType.id")
	public List<Map<String, Object>> findEquipmentTypeIdAndCountByActive(Boolean active);

	@Query("select equipment.groupId as groupId, count(distinct equipment.id) as count from EquipmentMapping equipmentMapping inner join Equipment equipment on equipmentMapping.equipment.id = equipment.id where equipment.active=?1 and equipmentMapping.active=?1 and equipment.equipmentType.id = ?2 group by equipment.groupId")
	public List<Map<String, Object>> findEquipmentGroupIdAndCountByActiveAndEquipmentTypeId(Boolean active,
			String equipmentTypeId);

	@Query("select equipment.groupId as groupId, equipmentType.id as equipmentTypeId, equipmentType.name as equipmentTypeName, equipmentType.code as equipmentTypeCode, equipmentMapping.arm.id as armId, equipmentMapping.lane.id as laneId, equipmentMapping.location.id as locationId, equipment.id as equipmentId from EquipmentMapping equipmentMapping inner join Equipment equipment on equipmentMapping.equipment.id = equipment.id inner join equipment.equipmentType equipmentType on equipmentType.id=equipment.equipmentType.id where equipment.active=?1 and equipmentMapping.active=?1 and equipmentMapping.location.id=?2 order by equipmentMapping.location.id, equipmentMapping.arm.id, equipmentMapping.lane.id, equipment.groupId")
	public List<Map<String, Object>> findEquipmentAndEquipmentTypeAndMappingByActiveAndLocationId(Boolean active,
			String locationId);

	@Query("select equipmentMapping.id from EquipmentMapping equipmentMapping inner join Equipment equipment on equipmentMapping.equipment.id = equipment.id where equipment.active=?1 and equipmentMapping.active=?1 and equipment.groupId=?2 and equipmentMapping.location.id=?3 and equipmentMapping.arm.id is null and equipmentMapping.lane.id is null order by equipment.id desc")
	public List<String> findIdsByActiveAndGroupIdAndLocationIdAndArmIdNullAndLaneIdNull(Boolean active, String groupId,
			String locationId);

	@Query("select equipmentMapping.id from EquipmentMapping equipmentMapping inner join Equipment equipment on equipmentMapping.equipment.id = equipment.id where equipment.active=?1 and equipmentMapping.active=?1 and equipment.groupId=?2 and equipmentMapping.location.id=?3 and equipmentMapping.arm.id =?4 and equipmentMapping.lane.id is null order by equipment.id desc")
	public List<String> findIdsByActiveAndGroupIdAndLocationIdAndArmIdAndLaneIdNull(Boolean active, String groupId,
			String locationId, String armId);

	@Query("select equipmentMapping.id from EquipmentMapping equipmentMapping inner join Equipment equipment on equipmentMapping.equipment.id = equipment.id where equipment.active=?1 and equipmentMapping.active=?1 and equipment.groupId=?2 and equipmentMapping.location.id=?3 and equipmentMapping.arm.id =?4 and equipmentMapping.lane.id =?5 order by equipment.id desc")
	public List<String> findIdsByActiveAndGroupIdAndLocationIdAndArmIdAndLaneId(Boolean active, String groupId,
			String locationId, String armId, String laneId);

	@Query("select id from EquipmentMapping where lane.id in ?1 and active=?2")
	public List<String> findIdByLaneIdsAndActive(List<String> laneIds, Boolean active);

	@Query("select id from EquipmentMapping where equipment.id in ?1")
	public List<String> findIdsByEquipmentIds(List<String> equipmentIds);

	@Query("select equipmentMapping.equipment.id from EquipmentMapping equipmentMapping where active=?1")
	public List<String> findEquipmentIdByActive(Boolean active);

	@Query("select equipmentMapping.equipment.id from EquipmentMapping equipmentMapping where id in ?1")
	public List<String> findEquipmentIdsByIds(List<String> ids);

	@Query(value = "select specificEquipment.equipmentId,specificEquipment.equipmentName,specificEquipment.groupId,specificEquipment.armId,specificEquipment.laneId,specificEquipment.equipmentTypeId from (select equipmentMapping.equipment_mst_id as equipmentId,equipment.name as equipmentName,equipmentMapping.arm_id as armId,equipmentMapping.lane_id as laneId,equipment.group_Id as groupId,equipment.equipment_Type_id as equipmentTypeId,equipmentTypeAttribute.is_Specific as isSpecific from Equipment_Mapping equipmentMapping inner join Equipment_mst equipment on equipmentMapping.equipment_mst_id=equipment.id inner join Equipment_Type_Attribute_mst equipmentTypeAttribute on equipment.equipment_Type_id=equipmentTypeAttribute.equipment_Type_id where equipmentMapping.location_mst_id=?1 and equipmentMapping.active=?2  group by equipmentTypeAttribute.equipment_Type_id,equipment.id order by equipmentTypeAttribute.equipment_Type_id) specificEquipment where specificEquipment.isSpecific=true order by specificEquipment.armId,specificEquipment.laneId", nativeQuery = true)
	public List<Map<String, Object>> findSpecificEquipmentByLocationIdAndActive(String locationId, Boolean active);

	@Query(value = "select count(equipments.groupId) count,equipments.* from (select equipmentMapping.arm_id as armId,equipmentMapping.lane_id as laneId,equipment.group_Id as groupId,equipment.equipment_type_id as equipmentTypeId,equipment_type_attribute_mst.is_specific as isSpecific from Equipment_Mapping equipmentMapping inner join Equipment_mst equipment on equipmentMapping.equipment_mst_id=equipment.id inner join equipment_type_attribute_mst on equipment.equipment_type_id=equipment_type_attribute_mst.equipment_type_id where equipmentMapping.location_mst_id=?1 and equipmentMapping.active=?2  group by equipment_type_attribute_mst.equipment_type_id,equipment.id order by equipment_type_attribute_mst.equipment_type_id) Equipments where Equipments.isSpecific=false  group by Equipments.armId,Equipments.laneId,Equipments.groupId order by Equipments.armId,Equipments.laneId", nativeQuery = true)
	public List<Map<String, Object>> findEquipmentByLocationIdAndActive(String locationId, Boolean active);

	@Query(value = "select location.id as locationId, location.name as locationName, location.locationType.name as locationTypeName, arm.id as armId, arm.name as armName, lane.id as laneId, lane.name as laneName from EquipmentMapping equipmentMapping inner join Location location on equipmentMapping.location.id = location.id left outer join Arm arm on equipmentMapping.arm.id = arm.id left outer join Lane lane  on equipmentMapping.lane.id = lane.id where equipmentMapping.equipment.id = ?1")
	public Tuple findByEquipmentId(String equipmentId);

	@Modifying
	@Query(value = "delete from EquipmentMapping where equipment.id in ?1")
	public int deleteByEquipmentIds(Set<String> ids);

}
