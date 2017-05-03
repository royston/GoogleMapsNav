package com.royston.google.maps;

import com.royston.google.maps.model.Place;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.File;

import static org.apache.log4j.Level.INFO;

/**
 * Created by Royston on 5/2/2017.
 */
@Path("geocode")
public class LatLong {
    static Logger logger = Logger.getLogger(LatLong.class);


    public static Configuration config;
    public LatLong(){
        Configurations configs = new Configurations();
        try {
            config = configs.properties(new File("C:\\Users\\Royston\\IdeaProjects\\GoogleMapsNav\\props.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Place getLatLong(@QueryParam("placeId") String placeId, @QueryParam("address") String address, @QueryParam("name") String name){
        logger.setLevel(INFO);
        Client client = Client.create();
        String apiUrl = config.getString("api.geocode.url");
        String apiKey = config.getString("api.key");
        WebResource webResource = client.resource(apiUrl);
        logger.info(webResource.getURI().getPath());
        ClientResponse response = webResource.queryParam("key", apiKey).queryParam("address",address).accept("application/json")
                .get(ClientResponse.class);
        if (response.getStatus() != 200) {
            System.out.print(response.getStatus() + response.getLength());
            logger.info(response.getStatus());
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
        String outputStr = response.getEntity(String.class);
        JSONObject obj = new JSONObject(outputStr);
        JSONArray predictions = obj.getJSONArray("results");
        Place[] places = new Place[predictions.length()];
        if(predictions.length() > 0){
            JSONObject geometry = predictions.getJSONObject(0).getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");
            Place place = new Place();
            place.setPlaceId(placeId);
            place.setName(name);
            place.setAddress(address);
            place.setLatitude(lat);
            place.setLongitude(lng);
            return place;
        }
        return null;
    }
}
