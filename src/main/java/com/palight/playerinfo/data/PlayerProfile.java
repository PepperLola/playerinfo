package com.palight.playerinfo.data;

import java.util.List;
import java.util.Map;

public class PlayerProfile {
    private String id;
    private String name;
    private PlayerProperties properties;

    public PlayerProfile() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerProperties getProperties() {
        return properties;
    }

    public void setProperties(PlayerProperties properties) {
        this.properties = properties;
    }
}
