package com.royston.google.maps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.royston.google.maps.model.Route;
import org.apache.log4j.ConsoleAppender;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;import com.royston.google.maps.model.google.Locations;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

import javax.measure.Measure;
import javax.measure.converter.UnitConverter;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;

import static javax.measure.unit.NonSI.MILE;
import static javax.measure.unit.NonSI.MINUTE;
import static javax.measure.unit.SI.KILOMETER;
import static javax.measure.unit.SI.METER;
import static org.apache.log4j.Level.INFO;
import static org.apache.log4j.Level.TRACE;

/**
 * Created by Royston on 4/12/2017.
 */
@Path("route")
public class RouteFinder {
    static Logger logger = Logger.getLogger(RouteFinder.class);

    public static Configuration config;
    public RouteFinder(){
        Configurations configs = new Configurations();
        try {
            config = configs.properties(new File("C:\\Users\\Royston\\IdeaProjects\\GoogleMapsNav\\props.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Route getOptimalRoute(@QueryParam("origin") String origin,
                                 @QueryParam("destination") String destination,
                                 @QueryParam("waypoints") String waypoints
                                ){
        org.apache.log4j.BasicConfigurator.configure();
        logger.setLevel(INFO);

        Route route1 = getRoute(origin, destination, waypoints);
        Route route2 = getRoute(origin, waypoints, destination);

        if(1 == 1 || route1.getTime() < route2.getTime()){
            return route1;
        }
        return route2;
    }
    private Route getRoute(String origin, String destination, String waypoints){
        Client client = Client.create();
        String apiUrl = config.getString("direction.api.url");
        String apiKey = config.getString("api.key");
        WebResource webResource = client.resource(apiUrl);
        ClientResponse response = webResource
                .queryParam("key", apiKey)
                .queryParam("origin",origin)
                .queryParam("destination", destination)
                .queryParam("waypoints", waypoints)
                .accept("application/json")
                .get(ClientResponse.class);
        if (response.getStatus() != 200) {
            System.err.println(response.getStatus());
            System.out.print(response.getStatus() + response.getLength());
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
        String outputStr = response.getEntity(String.class);
        JSONObject obj = new JSONObject(outputStr);
        JSONArray routes = obj.getJSONArray("routes");
        JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
        int totalDistance = 0;
        int totalDuration = 0;
        for(int legNo =0; legNo < legs.length(); ++legNo){
            JSONObject leg = legs.getJSONObject(legNo);
            int distance = leg.getJSONObject("distance").getInt("value");
            UnitConverter toMiles = METER.getConverterTo(MILE);
            distance = (int)toMiles.convert(Measure.valueOf(distance, METER).intValue(METER));

            int minutes = leg.getJSONObject("duration").getInt("value");

            totalDistance += distance;
            totalDuration += minutes;

        }

        totalDuration =  totalDuration / 60;
        int min = totalDuration % 60;
        int hours = totalDuration / 60;

        Route route = new Route();
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setWaypoint(waypoints);
        route.setDistance(totalDistance);
        route.setTimeHoursMins(hours == 0 ? min + " mins" : hours + " hours, " + min + " mins");
        route.setTime(totalDuration);
        return route;
    }
}
