package com.royston.google.maps.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by Royston on 4/22/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "placeId",
        "name",
        "address"
})
public class Places {
    @JsonProperty("places")
    private Place[] places;

    @JsonProperty("places")
    public Place[] getPlaces() {
        return places;
    }

    @JsonProperty("places")
    public void setPlaces(Place[] places) {
        this.places = places;
    }
}
