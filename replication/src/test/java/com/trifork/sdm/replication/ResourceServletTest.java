package com.trifork.sdm.replication;

import static com.trifork.sdm.replication.ReplicationTest.Day.YESTERDAY;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.junit.Test;

import com.trifork.sdm.models.AbstractRecord;


public class ResourceServletTest extends ReplicationTest {

	@Entity
	public static class Resource extends AbstractRecord {

		private Date validFrom = new Date();
		private String foo;


		public Resource() {

		}


		@Column
		public String getFoo() {

			return foo;
		}


		public void setFoo(String value) {

			this.foo = value;
		}


		@Override
		public Date getValidFrom() {

			return validFrom;
		}
	}


	@Override
	public void initialize() {

		//serve("/resource").with(servlet(Resource.class));
	}


	@Test
	public void should_reject_unaccepted_mime_type(URL resource) throws IOException {

		setParam("since", date(YESTERDAY));

		get("/resource");

		assertStatus(HTTP_OK);

		printContent();

		// assertXML("/resourceCollection/resource", is(not(null)));
	}
}
