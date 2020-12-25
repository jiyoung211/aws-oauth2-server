package com.jiyoung.apigw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "client")
public class Client
{
	@Id
	@Column(name = "clientid")
	private String clientId;

	@Column(name = "applicationdescription")
	private String applicationDescription;

	@Column(name = "applicationlogouri")
	private String applicationLogoUri;

	@Column(name = "applicationlogouturi")
	private String applicationLogoutUri;

	@Column(name = "applicationname")
	private String applicationName;

	@Column(name = "applicationweburi")
	private String applicationWebUri;

	@Column(name = "clientipaddress")
	private String clientIpAddress;

	@Column(name = "clientsecret")
	private String clientSecret;

	@Column(name = "confidential")
	private String confidential;

	@Column(name = "homerealm")
	private String homeRealm;

	@Column(name = "registeredat")
	private String registeredAt;

	@Column(name = "registereddynamically")
	private String registeredDynamically;

	@Column(name = "tokenendpointauthmethod")
	private String tokenEndpointAuthMethod;

	@Column(name = "resourceownersubject_id")
	private String resourceOwnerSubjectId;

	@Column(name = "subject_id")
	private String subjectId;
}
