package com.belaku.naveenprakash.npstreetmap;

import java.util.ArrayList;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
class Category{
    public int id;
    public String name;
    public String short_name;
    public String plural_name;
    public Icon icon;
}

class DropOff{
    public double latitude;
    public double longitude;
}

class Geocodes{
    public Main main;
    public Roof roof;
    public DropOff drop_off;
}

class Icon{
    public String prefix;
    public String suffix;
}

class Location{
    public String country;
    public String cross_street;
    public String formatted_address;
    public String postcode;
    public String address;
    public String locality;
    public String region;
    public String address_extended;
}

class Main{
    public double latitude;
    public double longitude;
}

class RelatedPlaces{
}

class Places{
    public String fsq_id;
    public ArrayList<Category> categories;
    public ArrayList<Object> chains;
    public String closed_bucket;
    public int distance;
    public Geocodes geocodes;
    public String link;
    public Location location;
    public String name;
    public RelatedPlaces related_places;
    public String timezone;
}

class Roof{
    public double latitude;
    public double longitude;
}

public class NearbyPlacesPojo{
    public ArrayList<Places> results;
}
