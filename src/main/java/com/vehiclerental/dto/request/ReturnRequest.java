package com.vehiclerental.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ReturnRequest {

    @NotNull(message = "Return odometer reading is required")
    @Positive(message = "Return odometer reading must be positive")
    private Double returnOdometer;

    private String returnFuelLevel;
    private String returnNotes;
    private String damageNotes;

    public ReturnRequest() {}

    public Double getReturnOdometer() { return returnOdometer; }
    public void setReturnOdometer(Double returnOdometer) { this.returnOdometer = returnOdometer; }

    public String getReturnFuelLevel() { return returnFuelLevel; }
    public void setReturnFuelLevel(String returnFuelLevel) { this.returnFuelLevel = returnFuelLevel; }

    public String getReturnNotes() { return returnNotes; }
    public void setReturnNotes(String returnNotes) { this.returnNotes = returnNotes; }

    public String getDamageNotes() { return damageNotes; }
    public void setDamageNotes(String damageNotes) { this.damageNotes = damageNotes; }
}
