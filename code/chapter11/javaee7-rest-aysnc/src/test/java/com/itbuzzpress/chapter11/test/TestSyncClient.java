package com.itbuzzpress.chapter11.test;

import static org.junit.Assert.*;

import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.itbuzzpress.chapter11.model.SimpleProperty;

public class TestSyncClient {
String BASE_URL ="http://localhost:8080/javaee7-rest-async/rest";
/*
	//@Test
	public void testParam() {
		Client client = ClientBuilder.newClient();
	 
		 
		SimpleProperty propertyIn = new SimpleProperty("mykey99","value");

		WebTarget myResource = client.target(BASE_URL+"/param/add");
		Response rs = myResource.request(MediaType.APPLICATION_XML)
						.post(Entity.xml(propertyIn), Response.class);
		
		assertEquals(rs.getStatus(),200);
		
		String out = rs.readEntity(String.class);
		SimpleProperty propertyOut = client
				.target(BASE_URL + "/param/xmlasync/{key}")
				.resolveTemplate("key", "mykey99")
				.request(MediaType.APPLICATION_XML)
		             .get(SimpleProperty.class);

		assertEquals("mykey99",propertyOut.getKey());
		assertEquals("value",propertyOut.getValue());
	}*/
	 
@Test	
public void testClientAsync() throws Exception {

   Client client = ClientBuilder.newClient();
   SimpleProperty propertyIn = new SimpleProperty("mykey","value");
 
   WebTarget myResource = client.target(BASE_URL+"/param/add");
   Future<SimpleProperty> ret =  myResource.request(MediaType.APPLICATION_XML).async()
				.post(Entity.xml(propertyIn), SimpleProperty.class);			   
   SimpleProperty property = ret.get();
   assertEquals("value", property.getValue());
   
   Future<SimpleProperty> future = client
			.target(BASE_URL + "/param/xmlasync/{key}")
			.resolveTemplate("key", "mykey")
			.request(MediaType.APPLICATION_XML).async()
			.get(SimpleProperty.class);

	SimpleProperty propertyOut = null;
		try {
			while (!future.isDone()) {
				Thread.sleep(100);
			}
			propertyOut = future.get();
		} catch (Exception e) {
				e.printStackTrace();
		}
	assertEquals("value", propertyOut.getValue());

}


}
