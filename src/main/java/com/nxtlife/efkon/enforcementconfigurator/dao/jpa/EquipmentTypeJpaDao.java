package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.type.EquipmentType;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeResponse;

public interface EquipmentTypeJpaDao extends JpaRepository<EquipmentType, String> {

	public Boolean existsByIdAndOrganizationId(String id, Long organizationId);
	
	public Boolean existsByCodeAndOrganizationId(String code, Long organizationId);

	public EquipmentTypeResponse findByOrganizationIdAndActiveAndId(Long organizationId, Boolean active, String id);

	public List<EquipmentTypeResponse> findByOrganizationId(Long organizationId);

	public List<EquipmentTypeResponse> findByOrganizationIdAndActive(Long organizationId, Boolean active);

	@Query(value = "select code from EquipmentType where id = ?1")
	public String findCodeById(String id);

	@Query(value = "select name from EquipmentType where id = ?1")
	public String findNameById(String id);

	@Query(value = "select quantity from EquipmentType where id = ?1")
	public Integer findQuantityById(String id);

	@Query(value = "select id from EquipmentType where organization.id=?1")
	public List<String> findAllIdsByOrganizationId(Long organizationId);
	
	@Query(value = "update EquipmentType set quantity = ?2 where id = ?1")
	public int updateQuantity(String id, Integer quantity);
}
