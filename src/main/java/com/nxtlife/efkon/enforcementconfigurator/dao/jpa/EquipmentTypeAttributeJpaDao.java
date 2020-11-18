package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.type.EquipmentTypeAttribute;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeAttributeResponse;

public interface EquipmentTypeAttributeJpaDao extends JpaRepository<EquipmentTypeAttribute, String> {

	public Boolean existsByNameAndEquipmentTypeId(String name, String equipmentTypeId);

	public List<EquipmentTypeAttributeResponse> findByEquipmentTypeIdAndActive(String equipmentTypeId, Boolean active);

	public EquipmentTypeAttributeResponse findResponseById(String id);

	@Query(value = "select count(id) from EquipmentTypeAttribute where equipmentType.id =?1")
	public Long findCountByEquipmentTypeId(String equipmentTypeId);

	@Modifying
	@Query(value = "update equipment_type_attribute_mst set name=:name, data_type=:dataType, is_specific=:isSpecific, options = :options, modified_by =:modifiedBy, modified_at =:modifiedAt where id=:id", nativeQuery = true)
	public int updateNameAndDataTypeAndIsSpecificAndOptionsById(@Param("name") String name,
			@Param("dataType") String dataType, @Param("isSpecific") Boolean isSpecific,
			@Param("options") String options, @Param("modifiedBy") Long modifiedBy, @Param("modifiedAt") Date date,
			@Param("id") String id);

	@Modifying
	@Query(value = "update EquipmentTypeAttribute set active = false, modifiedBy.id =?2, modifiedAt =?3 where id = ?1")
	public int delete(String id, Long userId, Date date);
}
