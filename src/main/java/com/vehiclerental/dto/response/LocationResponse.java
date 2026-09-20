package com.vehiclerental.dto.response;

import com.vehiclerental.entity.Location;

public class LocationResponse {
    private Long id;
    private String name;
    private String city;
    private String state;

    public LocationResponse() {}

    public LocationResponse(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.city = location.getCity();
        this.state = location.getState();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}
