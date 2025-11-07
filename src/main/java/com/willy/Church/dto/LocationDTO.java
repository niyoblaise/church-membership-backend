package com.willy.Church.dto;

import com.willy.Church.model.enums.LocationType;

import java.util.List;

public class LocationDTO {
    private Long id;
    private String name;
    private LocationType type;
    private String code;
    private List<LocationDTO> children;

    public LocationDTO() {
    }

    public LocationDTO(Long id, String name, LocationType type, String code) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.code = code;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<LocationDTO> getChildren() {
        return children;
    }

    public void setChildren(List<LocationDTO> children) {
        this.children = children;
    }
}
