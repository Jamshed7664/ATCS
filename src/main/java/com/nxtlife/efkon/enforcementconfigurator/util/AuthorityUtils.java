package com.nxtlife.efkon.enforcementconfigurator.util;

/**
 * <tt>AuthorityUtil</tt> contains all static <tt>Authority</tt>. It mainly used
 * with @Secured annotation. so who ever has that authority can access the code.
 * <p>
 * <p>
 * Add here whenever we added a new authority for user.
 * <p>
 *
 * @author ajay
 */
public final class AuthorityUtils {

	/**
	 * AUTHORITY TO FETCH AUTHORITY
	 */
	public static final String AUTHORITY_FETCH = "ROLE_AUTHORITY_FETCH";

	/**
	 * AUTHORITY TO FETCH ROLE
	 */
	public static final String ROLE_FETCH = "ROLE_ROLE_FETCH";

	/**
	 * AUTHORITY TO CREATE ROLE
	 */
	public static final String ROLE_CREATE = "ROLE_ROLE_CREATE";

	/**
	 * AUTHORITY TO UPDATE ROLE
	 */
	public static final String ROLE_UPDATE = "ROLE_ROLE_UPDATE";

	/**
	 * AUTHORITY TO DELETE ROLE
	 */
	public static final String ROLE_DELETE = "ROLE_ROLE_DELETE";

	/**
	 * AUTHORITY TO FETCH USER
	 */
	public static final String USER_FETCH = "ROLE_USER_FETCH";

	/**
	 * AUTHORITY TO CREATE USER
	 */
	public static final String USER_CREATE = "ROLE_USER_CREATE";

	/**
	 * AUTHORITY TO UPDATE USER
	 */
	public static final String USER_UPDATE = "ROLE_USER_UPDATE";

	/**
	 * AUTHORITY TO DELETE USER
	 */
	public static final String USER_DELETE = "ROLE_USER_DELETE";

	/**
	 * AUTHORITY TO FETCH LOCATION TYPE
	 */
	public static final String LOCATION_TYPE_FETCH = "ROLE_LOCATION_TYPE_FETCH";

	/**
	 * AUTHORITY TO SAVE LOCATION TYPE FIELD
	 */
	public static final String LOCATION_TYPE_CREATE = "ROLE_LOCATION_TYPE_CREATE";

	/**
	 * AUTHORITY TO UPDATE LOCATION TYPE FIELD
	 */
	public static final String LOCATION_TYPE_UPDATE = "ROLE_LOCATION_TYPE_UPDATE";

	/**
	 * AUTHORITY TO DELETE LOCATION TYPE FIELD
	 */
	public static final String LOCATION_TYPE_DELETE = "ROLE_LOCATION_TYPE_DELETE";

	/**
	 * AUTHORITY TO FETCH LOCATION
	 */
	public static final String LOCATION_FETCH = "ROLE_LOCATION_FETCH";

	/**
	 * AUTHORITY TO SAVE LOCATION FIELD
	 */
	public static final String LOCATION_CREATE = "ROLE_LOCATION_CREATE";

	/**
	 * AUTHORITY TO UPDATE LOCATION FIELD
	 */
	public static final String LOCATION_UPDATE = "ROLE_LOCATION_UPDATE";

	/**
	 * AUTHORITY TO DELETE LOCATION FIELD
	 */
	public static final String LOCATION_DELETE = "ROLE_LOCATION_DELETE";

	/**
	 * AUTHORITY TO FETCH EQUIPMENT TYPE AND EQUIPMENT TYPE FIELD
	 */
	public static final String EQUIPMENT_TYPE_FETCH = "ROLE_EQUIPMENT_TYPE_FETCH";

	/**
	 * AUTHORITY TO SAVE EQUIPMENT TYPE FIELD
	 */
	public static final String EQUIPMENT_TYPE_CREATE = "ROLE_EQUIPMENT_TYPE_CREATE";

	/**
	 * AUTHORITY TO UPDATE EQUIPMENT TYPE FIELD
	 */
	public static final String EQUIPMENT_TYPE_UPDATE = "ROLE_EQUIPMENT_TYPE_UPDATE";

	/**
	 * AUTHORITY TO DELETE EQUIPMENT TYPE FIELD
	 */
	public static final String EQUIPMENT_TYPE_DELETE = "ROLE_EQUIPMENT_TYPE_DELETE";

	/**
	 * AUTHORITY TO FETCH EQUIPMENT
	 */
	public static final String EQUIPMENT_FETCH = "ROLE_EQUIPMENT_FETCH";

	/**
	 * AUTHORITY TO SAVE EQUIPMENT
	 */
	public static final String EQUIPMENT_CREATE = "ROLE_EQUIPMENT_CREATE";

	/**
	 * AUTHORITY TO UPDATE EQUIPMENT
	 */
	public static final String EQUIPMENT_UPDATE = "ROLE_EQUIPMENT_UPDATE";

	/**
	 * AUTHORITY TO DELETE EQUIPMENT
	 */
	public static final String EQUIPMENT_DELETE = "ROLE_EQUIPMENT_DELETE";

	/**
	 * AUTHORITY TO FETCH EQUIPMENT MAPPED WITH LOCATION
	 */
	public static final String EQUIPMENT_MAPPING_FETCH = "ROLE_EQUIPMENT_MAPPING_FETCH";

	/**
	 * AUTHORITY TO SAVE EQUIPMENT MAPPED WITH LOCATION
	 */
	public static final String EQUIPMENT_MAPPING_CREATE = "ROLE_EQUIPMENT_MAPPING_CREATE";

	/**
	 * AUTHORITY TO FETCH CAMERA
	 */
	public static final String CAMERA_FETCH = "ROLE_CAMERA_FETCH";

	/**
	 * AUTHORITY TO UPDATE CAMERA
	 */
	public static final String CAMERA_UPDATE = "ROLE_CAMERA_UPDATE";

	/**
	 * AUTHORITY TO FETCH CAMERA LANES
	 */
	public static final String CAMERA_LANE_FETCH = "ROLE_CAMERA_LANE_FETCH";

	/**
	 * AUTHORITY TO DELETE CAMERA IMAGE
	 */
	public static final String CAMERA_IMAGE_DELETE = "ROLE_CAMERA_IMAGE_DELETE";

	/**
	 * AUTHORITY TO FETCH CAMERA COORDINATE
	 */
	public static final String IMAGE_COORDINATE_FETCH = "ROLE_IMAGE_COORDINATE_FETCH";

	/**
	 * AUTHORITY TO CREATE CAMERA COORDINATE
	 */
	public static final String IMAGE_COORDINATE_CREATE = "ROLE_IMAGE_COORDINATE_CREATE";

	/**
	 * AUTHORITY TO UPDATE CAMERA COORDINATE
	 */
	public static final String IMAGE_COORDINATE_UPDATE = "ROLE_IMAGE_COORDINATE_UPDATE";

	/**
	 * AUTHORITY TO CREATE ZONE INCIDENT
	 */
	public static final String ZONE_INCIDENT_CREATE = "ROLE_ZONE_INCIDENT_CREATE";

	/**
	 * AUTHORITY TO FETCH ZONE INCIDENT
	 */
	public static final String ZONE_INCIDENT_FETCH = "ROLE_ZONE_INCIDENT_FETCH";

	/**
	 * AUTHORITY TO DELETE CAMERA COORDINATE
	 */
	public static final String IMAGE_COORDINATE_DELETE = "ROLE_IMAGE_COORDINATE_DELETE";

	/**
	 * AUTHORITY TO FETCH INCIDENT
	 */
	public static final String INCIDENT_FETCH = "ROLE_INCIDENT_FETCH";

	/**
	 * AUTHORITY TO CREATE INCIDENT
	 */
	public static final String INCIDENT_CREATE = "ROLE_INCIDENT_CREATE";

	/**
	 * AUTHORITY TO CREATE CAMERA INCIDENT
	 */
	public static final String CAMERA_INCIDENT_CREATE = "ROLE_CAMERA_INCIDENT_CREATE";

	/**
	 * AUTHORITY TO FETCH CAMERA INCIDENT
	 */
	public static final String CAMERA_INCIDENT_FETCH = "ROLE_CAMERA_INCIDENT_FETCH";

}
