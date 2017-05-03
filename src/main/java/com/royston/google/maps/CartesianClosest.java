package com.royston.google.maps;

import com.royston.google.maps.model.Place;
import com.royston.google.maps.model.Places;
import com.sun.media.jfxmedia.Media;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;

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
    public Place getClosestByCartesianDistance(Places places){
        return null;
    }

}
