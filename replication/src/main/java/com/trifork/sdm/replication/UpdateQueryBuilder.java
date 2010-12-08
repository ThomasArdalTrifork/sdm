package com.trifork.sdm.replication;

import java.util.Date;

public class UpdateQueryBuilder {
	public class UpdateQuery {
		
		private final long pid;
		private final Date date;

		private UpdateQuery(long pid, Date date) {
			this.pid = pid;
			this.date = date;
			
		}

		public long getPID() {

			return pid;
		}

		public Date getDate() {

			return date;
		}
	}
	
	private Date date;
	private long pid;
	
	public UpdateQueryBuilder(String token) {
		
		try {
			String dateString = token.substring(0,9);
			long dateLong = Long.parseLong(dateString); 
			date = new Date(dateLong);
		
			String pidString = token.substring(10);
			pid = Long.parseLong(pidString);
		}
		catch (Throwable t) {
			// TODO: Do something meaningful.
		}
	}
	
	public UpdateQuery build() {
		
		return new UpdateQuery(pid, date);
	}
}
