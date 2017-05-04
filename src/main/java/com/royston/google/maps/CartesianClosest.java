package com.royston.google.maps;

import com.royston.google.maps.model.Place;
import com.royston.google.maps.model.Places;
import com.royston.google.maps.model.RouteOptions;
import com.sun.media.jfxmedia.Media;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.util.PriorityQueue;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Royston on 5/2/2017.
 */
@Path("cartesianclosest")
public class CartesianClosest {
    static Logger logger = Logger.getLogger(CartesianClosest.class);


    public static Configuration config;
    public CartesianClosest(){
        Configurations configs = new Configurations();
        try {
            config = configs.properties(new File("C:\\Users\\Royston\\IdeaProjects\\GoogleMapsNav\\props.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Place getClosestByCartesianDistance(RouteOptions data){
        if(data == null || data.getPlaces() == null || data.getPlaces().length == 0){
            return null;
        }
        //Slight chance that more than 1 place is same distance from route. In such case pick any
        //Too lazy to create custom object and insert into prioriy queue
        LatLong latLong = new LatLong();
        Place origin = latLong.getLatLong(null, data.getOrigin(), null);
        Place dest = latLong.getLatLong(null, data.getDestination(), null);

        double minDist = Double.MAX_VALUE;
        Place closest = null;
        for(Place place : data.getPlaces()){
            double dist = getCartesianDistanceFromPlaceToRoute(place, origin, dest);
            if(dist < minDist){
                minDist = dist;
                closest = place;
            }
        }
        return closest;
    }

    private double getCartesianDistanceFromPlaceToRoute(Place place, Place p1, Place p2){
        //Distance from a point to a line segment is 2 times area of triagle between the 3 points divided by length of line segment
        double areaSquared = Math.pow((p1.getLatitude() - p2.getLatitude()) * place.getLongitude()
                - (p1.getLongitude() - p2.getLongitude()) * place.getLatitude()
                + p2.getLatitude() * p1.getLongitude() - p1.getLatitude() * p2.getLongitude(), 2);//Ommiting the multiplication by2
        double lineLengthSq = Math.pow(p1.getLatitude() - p2.getLatitude(), 2)
                + Math.pow(p1.getLongitude() - p2.getLongitude(), 2);
        return areaSquared / lineLengthSq;
    }
}
