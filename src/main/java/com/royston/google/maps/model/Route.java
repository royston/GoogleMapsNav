package com.royston.google.maps.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "origin",
        "waypoint",
        "waypointName",
        "destination",
        "time",
        "distance"
})
/**
 * Created by Royston on 4/14/2017.
 */
public class Route {
    @JsonProperty("origin")
    private String origin;

    @JsonProperty("waypoint")
    private String waypoint;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("timeHoursMins")
    private String timeHoursMins;

    @JsonProperty("distance")
    private int distance;

    @JsonProperty("origin")
    public String getOrigin() {
        return origin;
    }

    @JsonProperty("origin")
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @JsonProperty("waypoint")
    public String getWaypoint() {
        return waypoint;
    }

    @JsonProperty("waypoint")
    public void setWaypoint(String waypoint) {
        this.waypoint = waypoint;
    }

    @JsonProperty("destination")
    public String getDestination() {
        return destination;
    }

    @JsonProperty("destination")
    public void setDestination(String destination) {
        this.destination = destination;
    }

    @JsonProperty("timeHoursMins")
    public String getTimeHoursMins() {
        return timeHoursMins;
    }

    @JsonProperty("timeHoursMins")
    public void setTimeHoursMins(String time) {
        this.timeHoursMins = time;
    }

    @JsonProperty("distance")
    public int getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(int distance) {
        this.distance = distance;
    }

    @JsonProperty("hasTolls")
    private boolean hasTolls;

    @JsonProperty("hasTolls")
    public boolean isHasTolls() {
        return hasTolls;
    }

    @JsonProperty("hasTolls")
    public void setHasTolls(boolean hasTolls) {
        this.hasTolls = hasTolls;
    }

    @JsonProperty("time")
    private int time;

    @JsonProperty("time")
    public int getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(int time) {
        this.time = time;
    }
}
