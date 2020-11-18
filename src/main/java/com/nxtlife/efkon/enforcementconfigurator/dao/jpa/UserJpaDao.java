package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.efkon.enforcementconfigurator.entity.user.User;
import com.nxtlife.efkon.enforcementconfigurator.view.user.UserResponse;

public interface UserJpaDao extends JpaRepository<User, Long> {

	public User findByUsernameAndOrganizationNull(String username);

	public User findByUsernameAndOrganizationId(String username, Long organizationId);

	public List<UserResponse> findByOrganizationId(Long organizationId);
	
	public UserResponse findResponseById(Long id);

	public Boolean existsByUsernameAndOrganizationId(String username, Long organizationId);

	public Boolean existsByContactNoOrEmailAndOrganizationIdAndContactNoNotNull(String contactNo, String email,
			Long organizationId);

	@Query(value = "select id from User where organization_id=?1 and contact_no = ?2 and active = ?3")
	public Long findIdByOrganizationIdAndContactNoAndActive(Long organizationId, String contactNo, Boolean active);

	@Query(value = "select id from User where organization_id=?1 and email = ?2 and active = ?3")
	public Long findIdByOrganizationIdAndEmailAndActive(Long organizationId, String email, Boolean active);

	@Query(value = "select email, contact_no as contactNo from user where username= ?1 and organization_id=?2", nativeQuery = true)
	public Map<String, String> findEmailAndContactByUsernameAndOrganizationId(String username, Long organizationId);

	@Query(value = "select password from user where id = ?1", nativeQuery = true)
	public String findPasswordById(Long id);

	@Query(value = "select generated_password from user where username = ?1 and organization_id = ?2", nativeQuery = true)
	public String findGeneratedPasswordByUsernameAndOrganizationId(String username, Long organizationId);

	@Modifying
	@Query(value = "update user set password=:password where username =:username and organization_id=:organizationId", nativeQuery = true)
	public int setPassword(@Param("username") String username, @Param("organizationId") Long organizationId,
			@Param("password") String password);

	@Modifying
	@Query(value = "update user set password=:password where id =:id", nativeQuery = true)
	public int setPassword(@Param("id") Long id, @Param("password") String password);

	@Modifying
	@Query(value = "update user set generated_password=:password where username =:username and organization_id=:organizationId", nativeQuery = true)
	public int setGeneratedPassword(@Param("username") String username, @Param("organizationId") Long organizationId,
			@Param("password") String password);

	@Modifying
	@Query(value = "update User set active=true, modified_by =?2, modified_at =?3 where id =?1")
	public int activate(Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update User set active=false, modified_by =?2, modified_at =?3 where id =?1")
	public int delete(Long id, Long userId, Date date);

}
