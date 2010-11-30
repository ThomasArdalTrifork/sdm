package com.trifork.sdm.replication.security;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AuthorizationBuilder {

	public class Authorization {

		// TODO: Inject this.
		private final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHss");
		
		private Class<?> entity;
		private int entityVersion;

		private Date expirationDate;

		/**
		 * Denotes that a client has no limit on the number of records he wants
		 * returned.
		 */
		public final static int NO_PAGE_LIMIT = -1;


		/**
		 * The type of the requested entity.
		 * 
		 * @return The entity class.
		 */
		public Class<?> getEntity() {

			return entity;
		}


		/**
		 * The version of the entity schema the client wants results for.
		 * 
		 * Entities are delivered on a best effort basis. This means
		 * 
		 * If an unsupported version is requested the client will receive a '403
		 * Forbidden' and a message describing the situation.
		 */
		public int getEntityVersion() {

			return entityVersion;
		}

		/**
		 * The date past which the authorization is no longer valid.
		 */
		public Date getExpirationDate() {

			return expirationDate;
		}
		
		@Override
		public String toString() {
			
			final StringBuilder builder = new StringBuilder();
			
			builder.append(entity.getSimpleName());
			builder.append(',');
			builder.append(entityVersion);
			builder.append(',');
			builder.append(formatter.format(expirationDate));
			
			return builder.toString();
		}
	}

	
	private Class<?> entity;
	private int ttl;
	private int entityVersion;


	public AuthorizationBuilder setEntity(Class<?> entity) {

		this.entity = entity;

		return this;
	}


	public AuthorizationBuilder setTTL(int ttl) {

		this.ttl = ttl;

		return this;
	}


	public AuthorizationBuilder setEntityVersion(int entityVersion) {

		this.entityVersion = entityVersion;

		return this;
	}


	Authorization build() {

		// Validation
		
		assert entity != null : "Entity must not be null.";
		assert ttl > 0 : "TTL must be a positive integer.";
		
		// There are no restrictions on the range of entity versions
		// (e.g. -123 is a valid entity version, though it might not exist).
		
		Authorization authorization = new Authorization();

		authorization.entity = entity;
		authorization.entityVersion = entityVersion;

		Calendar expirationDate = Calendar.getInstance();
		expirationDate.add(Calendar.MINUTE, ttl);
		authorization.expirationDate = expirationDate.getTime();
		
		return authorization;
	}
}
