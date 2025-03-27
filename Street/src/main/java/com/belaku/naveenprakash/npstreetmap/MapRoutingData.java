package com.belaku.naveenprakash.npstreetmap;

import java.util.ArrayList;

class MapRoutingData {
    ArrayList<Routes> routes = new ArrayList<Routes>();
}

class Routes {
    ArrayList<Legs> legs = new ArrayList<Legs>();
}

class Legs {
    Distance distance = new Distance();
    Duration duration = new Duration();
    String end_address = "";
    String start_address = "";
    LocationRoute end_location = new LocationRoute();
    LocationRoute start_location = new LocationRoute();
    ArrayList<Steps> steps = new ArrayList<Steps>();
}

class Steps {
    Distance distance = new Distance();
    Duration duration = new Duration();
    String end_address = "";
    String start_address = "";
    LocationRoute end_location = new LocationRoute();
    LocationRoute start_location = new LocationRoute();
    PolyLine polyline = new PolyLine();
    String travel_mode = "";
    String maneuver = "";
}

class Duration {
    String text = "";
    int value = 0;
}

class Distance {
    String text = "";
    int value = 0;
}

class PolyLine {
    String points = "";
}

class LocationRoute {
    String lat = "";
    String lng = "";
}