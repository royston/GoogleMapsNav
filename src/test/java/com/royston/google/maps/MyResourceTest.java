package com.royston.google.maps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.log4j.Level.INFO;
import static org.junit.Assert.assertEquals;

public class MyResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        org.apache.log4j.BasicConfigurator.configure();
        Logger LOG = Logger.getLogger(Location.class);
        LOG.setLevel(INFO);

//        String responseMsg = target.path("myresource").request().get(String.class);
//        assertEquals("Got it!", responseMsg);

//        String resonse = target.path("location").queryParam("location", "Chipotle").request().get(String.class);
        String resonse = target.path("route").queryParam("origin", "Santa Clara, CA")
                .queryParam("destination", "San Francisco, CA")
                .queryParam("waypoints", "San Bruno, CA")
                .request().get(String.class);
        System.out.print(resonse);
    }
}
