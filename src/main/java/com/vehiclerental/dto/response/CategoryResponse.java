package com.vehiclerental.dto.response;

import com.vehiclerental.entity.VehicleCategory;

public class CategoryResponse {
    private Long id;
    private String name;
    private String description;

    public CategoryResponse() {}

    public CategoryResponse(VehicleCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
