package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.entity.user.Authority;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.view.user.security.AuthorityResponse;

public interface AuthorityService {

	public void save(Authority authority);

	/**
	 * this method used to fetch authority using id
	 * 
	 * @param id
	 * @return <tt>Authority</tt>
	 * @throws NotFoundException
	 *             if authority not found
	 */
	public Authority findById(long id);

	/**
	 * this method used to fetch all authorities
	 * 
	 * @return list of <tt>AuthorityResponse</tt>
	 */
	public List<AuthorityResponse> getAllAuthorities();

	/**
	 * this method used to fetch all authorities by role
	 * 
	 * @param roleId
	 * @return list of <tt>AuthorityResponse</tt>
	 * @throws NotFoundException
	 *             if role id not found
	 */
	public List<AuthorityResponse> getAllAuthoritiesByRoleId(Long roleId);

}
