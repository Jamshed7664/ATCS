package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.AuthorityJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.OrganizationJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.RoleAuthorityJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.RoleJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.UserJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.UserRoleJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.RoleAuthorityKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.UserRoleKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.organization.Organization;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.Authority;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.Role;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.RoleAuthority;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.User;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.UserRole;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.UserService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.user.UserRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.user.UserResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.user.security.AuthorityResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.user.security.PasswordRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.user.security.RoleResponse;

@Service("userService")
@Transactional
public class UserServiceImpl extends BaseService implements UserDetailsService, UserService {

	@Value("${efkon.organization.name}")
	private String organizationName;

	@Value("${efkon.organization.code}")
	private String organizationCode;

	@Value("${efkon.organization.latitude}")
	private String organizationLatitude;

	@Value("${efkon.organization.longitude}")
	private String organizationLongitude;

	@Autowired
	private UserJpaDao userJpaDao;

	@Autowired
	private OrganizationJpaDao organizationDao;

	@Autowired
	private RoleJpaDao roleDao;

	@Autowired
	private UserRoleJpaDao userRoleJpaDao;

	@Autowired
	private AuthorityJpaDao authorityDao;

	@Autowired
	private RoleAuthorityJpaDao roleAuthorityDao;

	@Autowired
	private HttpServletRequest httpServletRequest;

	private static PasswordEncoder userPasswordEncoder = new BCryptPasswordEncoder();

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

	@PostConstruct
	public void init() {
		Organization organization;
		if ((organization = organizationDao.findByCode(organizationCode)) == null) {
			organization = new Organization();
			organization.setName(organizationName);
			organization.setCode(organizationCode);
			organization.setCentreLatitude(organizationLatitude);
			organization.setCentreLongitude(organizationLongitude);
			organizationDao.save(organization);
		}

		Role role;
		if ((role = roleDao.findByOrganizationIdAndName(organization.getId(), "SuperAdmin")) == null) {
			role = new Role("SuperAdmin");
			role.setOrganization(organization);
		}
		if (userJpaDao.findByUsernameAndOrganizationId("ajay", organization.getId()) == null) {
			User user = new User("ajay", userPasswordEncoder.encode("12345"), null);
			user.setName("Ajay");
			user.setOrganization(organization);
			user.setUserRoles(Arrays.asList(new UserRole(new UserRoleKey(role.getId(), user.getId()), role, user)));
			userJpaDao.saveAll(Arrays.asList(user));
			List<Authority> authorities = authorityDao.findAll();
			for (Authority authority : authorities) {
				roleAuthorityDao.save(
						new RoleAuthority(new RoleAuthorityKey(role.getId(), authority.getId()), role, authority));
			}
		}

	}

	/**
	 * this method used to validate role ids that these role ids exist in
	 * database or not for an organization
	 * 
	 * @param requestRoleIds
	 * @param organizationId
	 */
	private void validateRoleIds(Set<Long> requestRoleIds, Long organizationId) {
		List<Long> roleIds = roleDao.getAllIdsByActive(true);
		if (!roleIds.containsAll(requestRoleIds)) {
			requestRoleIds.removeAll(roleIds);
			throw new ValidationException(String.format("Some of the roles (%s) are not valid or not active",
					StringUtils.arrayToCommaDelimitedString(requestRoleIds.toArray())));

		}
	}

	/**
	 * this method used to validate user request like username already exist or
	 * not
	 * 
	 * @param request
	 * @param organizationId
	 */
	private void validate(UserRequest request, Long organizationId) {
		if (userJpaDao.existsByUsernameAndOrganizationId(request.getUsername(), organizationId)) {
			throw new ValidationException(String.format("This user (%s) already exist", request.getUsername()));
		} else if (userJpaDao.findIdByOrganizationIdAndContactNoAndActive(organizationId, request.getContactNo(),
				true) != null) {
			throw new ValidationException(String.format("This user's contactNo (%s) already exists",
					request.getContactNo() == null ? "NA" : request.getContactNo()));
		} else if (userJpaDao.findIdByOrganizationIdAndEmailAndActive(organizationId, request.getEmail(),
				true) != null) {
			throw new ValidationException(String.format("This user's email (%s) already exists", request.getEmail()));
		}
		validateRoleIds(request.getRoleIds(), organizationId);
	}

	/**
	 * this method used to fetch user response using user entity and roleIds
	 * 
	 * @param user
	 * @param roleIds
	 * @return {@link UserResponse}
	 */
	private UserResponse fetch(User user, Set<Long> roleIds) {
		UserResponse response = UserResponse.get(user);
		response.setRoles(new HashSet<>());
		RoleResponse roleResponse;
		for (Long roleId : roleIds) {
			roleResponse = roleDao.findResponseById(roleId);
			roleResponse.setAuthorities(authorityDao.findByAuthorityRolesRoleId(roleId));
			response.getRoles().add(roleResponse);
		}
		return response;
	}

	@Override
	@Secured(AuthorityUtils.USER_CREATE)
	public UserResponse save(UserRequest request) {
		Long organizationId = getUser().gettOrganizationId();
		validate(request, organizationId);
		User user = request.toEntity();
		user.settOrganizationId(organizationId);
		user.setPassword(userPasswordEncoder.encode("12345"));
		user = userJpaDao.save(user);
		for (Long roleId : request.getRoleIds()) {
			userRoleJpaDao.save(user.getId(), roleId);
		}
		return fetch(user, request.getRoleIds());
	}

	@Override
	@Secured(AuthorityUtils.USER_FETCH)
	public List<UserResponse> findAll() {
		Long organizationId = getUser().gettOrganizationId();
		List<UserResponse> userResponseList = userJpaDao.findByOrganizationId(organizationId);
		Set<Long> roleIds = roleDao.findIdsByOrganizationIdAandActive(organizationId, true);
		Map<Long, List<AuthorityResponse>> roleAuthoritiesMap = new HashMap<>();
		for (Long roleId : roleIds) {
			roleAuthoritiesMap.put(roleId, authorityDao.findByAuthorityRolesRoleId(roleId));
		}
		for (UserResponse user : userResponseList) {
			user.setRoles(roleDao.findByRoleUsersUserId(unmask(user.getId())));
			for (RoleResponse role : user.getRoles()) {
				role.setAuthorities(roleAuthoritiesMap.get(unmask(role.getId())));
			}
		}
		return userResponseList;
	}

	@Override
	@Secured(AuthorityUtils.USER_FETCH)
	public UserResponse findById(Long id) {
		Long unmaskId = unmask(id);
		Long organizationId = getUser().gettOrganizationId();
		UserResponse user = userJpaDao.findResponseById(unmaskId);
		if (user == null) {
			throw new NotFoundException(String.format("User(%d) not found", id));
		}
		if (!user.getOrganizationId().equals(organizationId)) {
			throw new AccessDeniedException("You aren't login with correct user to fetch this details");
		}
		user.setRoles(roleDao.findByRoleUsersUserId(unmask(user.getId())));
		for (RoleResponse role : user.getRoles()) {
			role.setAuthorities(authorityDao.findByAuthorityRolesRoleId(unmask(role.getId())));
		}
		return user;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String client = httpServletRequest.getUserPrincipal().getName();
		if (client == null) {
			throw new ValidationException("Client header not found");
		}
		Long organizationId = organizationDao.findIdByName(client);
		if (organizationId == null) {
			throw new NotFoundException(String.format("Client(%s) not found", client));
		}
		User user = userJpaDao.findByUsernameAndOrganizationId(username, organizationId);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("username(%s) not found", username));
		} else if (!user.getActive()) {
			throw new ValidationException("User is deactivated. Please ask admin to activate again to access.");
		}
		user.setUserId(user.getId());
		Set<Authority> authorities = new HashSet<>();
		Set<Role> roles = new HashSet<>();
		for (UserRole userRole : user.getUserRoles()) {
			for (RoleAuthority roleAuthority : userRole.getRole().getRoleAuthorities()) {
				authorities.add(roleAuthority.getAuthority());
				roles.add(roleAuthority.getRole());
			}
		}
		if (user.getOrganization() != null)
			user.settOrganizationId(user.getOrganization().getId());
		user.setAuthorities(authorities);
		user.setRoles(roles);
		return user;
	}

	@Override
	public UserResponse findByAuthentication() {
		User user = getUser();
		if (user == null) {
			throw new NotFoundException("User not found");
		}
		UserResponse response = UserResponse.get(user);
		response.setOrganization(organizationDao.findResponseById(user.gettOrganizationId()));
		return response;
	}

	@Override
	@Secured(AuthorityUtils.USER_UPDATE)
	public UserResponse update(Long userId, UserRequest request) {
		Long organizationId = getUser().gettOrganizationId();
		Long unmaskUserId = unmask(userId);
		Boolean requestBody = false;
		if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
			validateRoleIds(request.getRoleIds(), organizationId);
			requestBody = true;
		}
		if (request.getContactNo() == null && request.getEmail() == null && request.getName() == null && !requestBody) {
			throw new ValidationException("Request body is not valid");
		}
		Long id;
		Optional<User> user = userJpaDao.findById(unmaskUserId);
		if (!user.isPresent()) {
			throw new NotFoundException(String.format("User (%s) not found", userId));
		}
		if (request.getContactNo() != null) {
			if (request.getContactNo().length() != 10 || !Pattern.matches("^[0-9]*$", request.getContactNo())) {
				throw new ValidationException("Contact number value is not correct");
			}
			id = userJpaDao.findIdByOrganizationIdAndContactNoAndActive(organizationId, request.getContactNo(), true);
			if (id != null && !id.equals(unmaskUserId)) {
				throw new ValidationException(
						String.format("This contact number (%s) already exists", request.getContactNo()));
			}
			user.get().setContactNo(request.getContactNo());
		}
		if (request.getEmail() != null) {
			id = userJpaDao.findIdByOrganizationIdAndEmailAndActive(organizationId, request.getEmail(), true);
			if (id != null && !id.equals(unmaskUserId)) {
				throw new ValidationException(String.format("This email (%s) already exists", request.getEmail()));
			}
			user.get().setEmail(request.getEmail());
		}
		if (request.getName() != null) {
			user.get().setName(request.getName());
		}
		userJpaDao.save(user.get());
		Set<Long> roleIds = userRoleJpaDao.findRoleIdsByUserId(unmaskUserId);
		if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
			Set<Long> requestedRoleIds = new HashSet<>(request.getRoleIds());
			Set<Long> newRoles = request.getRoleIds();
			newRoles.removeAll(roleIds);
			for (Long roleId : newRoles) {
				userRoleJpaDao.save(unmaskUserId, roleId);
			}
			roleIds.removeAll(requestedRoleIds);
			for (Long roleId : roleIds) {
				userRoleJpaDao.delete(unmaskUserId, roleId);
			}
		}
		return fetch(user.get(), request.getRoleIds() == null ? roleIds : request.getRoleIds());
	}

	@Override
	public SuccessResponse forgotPassword(String username) {
		if (!username.matches("^[@A-Za-z0-9_]{3,20}$")) {
			throw new ValidationException(String.format("Incorrect username [%s]", username));
		}
		String client = httpServletRequest.getHeader("clientId");
		if (client == null) {
			throw new ValidationException("Client header not found");
		}
		Long organizationId = organizationDao.findIdByName(client);
		if (organizationId == null) {
			throw new NotFoundException(String.format("Organization(%s) not found", client));
		}
		Map<String, String> response = userJpaDao.findEmailAndContactByUsernameAndOrganizationId(username,
				organizationId);
		if (response == null) {
			throw new NotFoundException(String.format("User[username-%s] not found", username));
		}
		if (response.get("email") == null && response.get("contactNo") == null) {
			throw new ValidationException("User email/contact not register with us");
		}
		String generatedPassword = UUID.randomUUID().toString().substring(0, 6);
		logger.info("Password {} has been sent to email {}/contact {}", generatedPassword, response.get("email"),
				response.get("contactNo"));
		userJpaDao.setGeneratedPassword(username, organizationId, userPasswordEncoder.encode(generatedPassword));
		return new SuccessResponse(HttpStatus.OK.value(),
				"New generated password has been sent to your email and contact number");
	}

	@Override
	public SuccessResponse matchGeneratedPassword(String username, String generatedPassword) {
		String client = httpServletRequest.getHeader("clientId");
		if (client == null) {
			throw new ValidationException("Client header not found");
		}
		Long organizationId = organizationDao.findIdByName(client);
		if (organizationId == null) {
			throw new NotFoundException(String.format("Organization(%s) not found", client));
		}
		String encodedGeneratedPassword = userJpaDao.findGeneratedPasswordByUsernameAndOrganizationId(username,
				organizationId);
		if (encodedGeneratedPassword == null) {
			throw new NotFoundException(String.format("User[username-%s] not found or password already set", username));
		}
		if (!userPasswordEncoder.matches(generatedPassword, encodedGeneratedPassword)) {
			throw new ValidationException(String.format("Sent generated password[%s] incorrect", generatedPassword));
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Generated password matched");
	}

	@Override
	public SuccessResponse changePasswordByGeneratedPassword(PasswordRequest request) {
		request.checkGeneratedPassword();
		String client = httpServletRequest.getHeader("clientId");
		if (client == null) {
			throw new ValidationException("Client header not found");
		}
		Long organizationId = organizationDao.findIdByName(client);
		if (organizationId == null) {
			throw new NotFoundException(String.format("Organization(%s) not found", client));
		}
		SuccessResponse response = matchGeneratedPassword(request.getUsername(), request.getGeneratedPassword());
		if (response.getStatus() != HttpStatus.OK.value()) {
			throw new ValidationException("Generated password didn't match");
		}
		int rows = userJpaDao.setPassword(request.getUsername(), organizationId,
				userPasswordEncoder.encode(request.getPassword()));
		if (rows == 0) {
			throw new ValidationException("No row updated");
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Password changed successfully");
	}

	@Override
	public SuccessResponse changePassword(PasswordRequest request) {
		request.checkPassword();
		String encodedPassword = userJpaDao.findPasswordById(getUserId());
		if (encodedPassword == null) {
			throw new NotFoundException(
					String.format("User[id-%s] not found or password already set", mask(getUserId())));
		}
		if (!userPasswordEncoder.matches(request.getOldPassword(), encodedPassword)) {
			throw new ValidationException(String.format("Old password[%s] incorrect", request.getOldPassword()));
		}
		int rows = userJpaDao.setPassword(getUserId(), userPasswordEncoder.encode(request.getPassword()));
		if (rows == 0) {
			throw new ValidationException("No row updated");
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Password changed successfully");
	}

	@Override
	@Secured(AuthorityUtils.USER_UPDATE)
	public SuccessResponse activate(Long id) {
		Long unmaskId = unmask(id);
		if (!userJpaDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("User (%s) not found", id));
		}
		if (userRoleJpaDao.findRoleIdsByUserIdAndRoleActive(unmaskId, false).size() > 0) {
			throw new ValidationException("Some of the roles are not active for this user");
		}
		int rows = userJpaDao.activate(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("User {} activated successfully", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "User activated successfully");
	}

	@Override
	@Secured(AuthorityUtils.USER_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		if (!userJpaDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("User (%s) not found", id));
		}
		int rows = userJpaDao.delete(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("User {} deleted successfully", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "User deleted successfully");
	}

}
