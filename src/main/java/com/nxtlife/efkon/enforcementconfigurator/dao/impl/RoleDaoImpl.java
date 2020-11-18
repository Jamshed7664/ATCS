package com.nxtlife.efkon.enforcementconfigurator.dao.impl;

import org.springframework.stereotype.Repository;

import com.nxtlife.efkon.enforcementconfigurator.dao.RoleDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.Role;

@Repository("roleDaoImpl")
public class RoleDaoImpl extends BaseDao<Long, Role> implements RoleDao {

}
