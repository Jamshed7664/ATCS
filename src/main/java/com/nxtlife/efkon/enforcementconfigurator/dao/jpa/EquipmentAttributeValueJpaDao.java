package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.EquipmentAttributeValue;

public interface EquipmentAttributeValueJpaDao extends JpaRepository<EquipmentAttributeValue, String> {

	public Boolean existsByEquipmentTypeAttributeIdAndActiveAndEquipmentActive(String equipmentTypeAttributeId,
			Boolean active, Boolean locationActive);

	@Query(value = "select equipment_type_attribute_id as attributeId, equipment.id as equipmentId, equipment.name as equipmentName, value as value from equipment_attribute_value equipmentAttributeValue inner join equipment_mst equipment on equipmentAttributeValue.equipment_id = equipment.id where equipment.group_id=?1 and equipmentAttributeValue.active =?2", nativeQuery = true)
	public List<Map<String, Object>> findAttributeIdAndValueAndEquipmentIdAndEquipmentNameByEquipmentGroupIdAndActive(
			String equipmentGroupId, Boolean active);
	
	@Query(value = "select equipment_type_attribute_id as attributeId, equipment.id as equipmentId, equipment.name as equipmentName, value as value from equipment_attribute_value equipmentAttributeValue inner join equipment_mst equipment on equipmentAttributeValue.equipment_id = equipment.id left outer join equipment_mapping equipment_mapping on equipment_mapping.equipment_mst_id = equipment.id where equipment.group_id=?1 and equipmentAttributeValue.active =?2 and (equipment_mapping.location_mst_id=?3 or equipment_mapping.location_mst_id is null)", nativeQuery = true)
	public List<Map<String, Object>> findAttributeIdAndValueAndEquipmentIdAndEquipmentNameByEquipmentGroupIdAndActiveAndLocationId(
			String equipmentGroupId, Boolean active, String locationId);

	@Query(value = "select equipmentTypeAttribute.id as attributeId, value as value from EquipmentAttributeValue where equipment.id=?1")
	public List<Map<String, Object>> findAttributeIdAndValueByEquipmentId(String equipmentId);

	@Modifying
	@Query(value = "update EquipmentAttributeValue set value = ?2 where equipmentTypeAttribute.id = ?1 and equipment.id in ?3")
	public int update(String equipmentTypeAttributeId, String value, Set<String> equipmentIds);

	@Modifying
	@Query(value = "update EquipmentAttributeValue set value = ?2 where equipmentTypeAttribute.id = ?1 and equipment.id = ?3")
	public int update(String equipmentTypeAttributeId, String value, String equipmentId);

	@Modifying
	@Query(value = "update EquipmentAttributeValue set active =false, modified_by =?2, modified_at =?3 where equipment.id in ?1")
	public int delete(List<String> equipmentIds, Long userId, Date date);
}
