package com.trifork.sdm.replication;

import static com.trifork.sdm.replication.ReplicationTest.Day.YESTERDAY;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.junit.Test;

import com.trifork.sdm.models.AbstractRecord;

public class ResourceServletTest extends ReplicationTest {
	
	@Entity
	protected class Resource extends AbstractRecord {
		
		@Column
		public String getFoo() { return "foo"; }

		@Override
		public Date getValidFrom() {

			return null;
		}
	}
	
	
	@Override
	public void initialize(ConnectionManager manager) {
		
		serve("/resource").with(servlet(Resource.class));
	}


	@Test
	public void should_reject_unaccepted_mime_type(URL resource) throws IOException {
		
		setParam("since", date(YESTERDAY));
		
		get("/resource");
		
		assertStatus(HTTP_OK);
		
		assertXML("/resourceCollection/resource", is(not(null)));
	}
}
