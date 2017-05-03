package com.royston.google.maps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.royston.google.maps.model.Place;
import com.royston.google.maps.model.Places;
import com.royston.google.maps.model.google.Locations;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.media.jfxmedia.Media;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.log4j.Level.INFO;

/**
 * Created by Royston on 4/10/2017.
 */
@Path("location")
public class Location {
    static Logger logger = Logger.getLogger(Location.class);


    public static Configuration config;
    public Location(){
        Configurations configs = new Configurations();
        try {
            config = configs.properties(new File("C:\\Users\\Royston\\IdeaProjects\\GoogleMapsNav\\props.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Places getLocationMatches(@QueryParam("location") String location){
        org.apache.log4j.BasicConfigurator.configure();
        logger.setLevel(INFO);
        Client client = Client.create();
        String apiUrl = config.getString("api.url");
        String apiKey = config.getString("api.key");
        WebResource webResource = client.resource(apiUrl);
        logger.info(webResource.getURI().getPath());
        ClientResponse response = webResource.queryParam("key", apiKey).queryParam("input",location).accept("application/json")
                .get(ClientResponse.class);
        if (response.getStatus() != 200) {
            System.out.print(response.getStatus() + response.getLength());
            logger.info(response.getStatus());
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
        String outputStr = response.getEntity(String.class);
        JSONObject obj = new JSONObject(outputStr);
        JSONArray predictions = obj.getJSONArray("predictions");
        Place[] places = new Place[predictions.length()];
        for(int i=0; i<predictions.length(); ++i){
            JSONObject prediction = predictions.getJSONObject(i);
            String placeId = prediction.getString("place_id");
            JSONArray terms = prediction.getJSONArray("terms");
            String name = terms.getJSONObject(0).getString("value");
            String address = prediction.getString("description");

            places[i] = new Place();
            places[i].setPlaceId(placeId);
            places[i].setName(name);
            places[i].setAddress(address);
        }
        Places placesObj = new Places();
        placesObj.setPlaces(places);

        return placesObj;
    }

    private void insertCache(String location, String doc){


        try {
            File file = new File(config.getString("cache.location") + location + ".json");
            FileUtils.touch(file);
            FileUtils.writeStringToFile(file, doc);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Locations getCachedLocation(String location){
        try {
            File file = new File(config.getString("cache.location") + location + ".json");
            String doc = FileUtils.readFileToString(file);
            ObjectMapper objectMapper = new ObjectMapper();
            Locations loc = objectMapper.readValue(doc, Locations.class);
            return loc;
        } catch (IOException e) {

        }
        return null;
    }
}
