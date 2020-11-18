package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.OauthClientDetailsJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.oauth.OauthClientDetails;
import com.nxtlife.efkon.enforcementconfigurator.service.OauthClientService;

@Service("oauthClientService")
public class OauthClientServiceImpl implements OauthClientService {

	@Autowired
	private OauthClientDetailsJpaDao oauthClientDetailsDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() {
		if (oauthClientDetailsDao.findByClientId("Aligarh") == null) {
			OauthClientDetails oauthClientDetails = new OauthClientDetails();
			oauthClientDetails.setAccessTokenValidity(-1);
			oauthClientDetails.setScope("read,write");
			oauthClientDetails.setClientId("Aligarh");
			oauthClientDetails.setAuthorizedGrantTypes("password");
			oauthClientDetails.setAutoapprove("1");
			oauthClientDetails.setClientSecret(passwordEncoder.encode("nxtlife"));
			oauthClientDetails.setRefreshTokenValidity(-1);
			oauthClientDetails.setResourceIds("enforcement-configurator-api");
			oauthClientDetailsDao.save(oauthClientDetails);
		}
	}

}
