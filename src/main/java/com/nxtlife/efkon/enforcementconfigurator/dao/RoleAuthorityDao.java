package com.nxtlife.efkon.enforcementconfigurator.dao;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.entity.user.RoleAuthority;

public interface RoleAuthorityDao {
	
	public RoleAuthority save(RoleAuthority roleAuthority);

	public int deleteByRoleIdAndAuthorityIds(Long roleId, List<Long> authorityIds);

}
