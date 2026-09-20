package com.vehiclerental.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LocationRequest {
    @NotBlank(message = "Location name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    private String state;

    public LocationRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}
