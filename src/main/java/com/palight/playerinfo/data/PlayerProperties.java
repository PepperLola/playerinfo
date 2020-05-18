package com.palight.playerinfo.data;

import java.util.Map;

public class PlayerProperties {
    private long timestamp;
    private String profileId;
    private String profileName;
    private Map<String, Map<String, Object>> textures;

    public PlayerProperties() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Map<String, Map<String, Object>> getTextures() {
        return textures;
    }

    public void setTextures(Map<String, Map<String, Object>> textures) {
        this.textures = textures;
    }

    public String getSkinUrl() {
        return (String) textures.get("SKIN").get("url");
    }

    public Map<String, String> getSkinMetadata() {
        if (!textures.get("SKIN").containsKey("metadata")) return null;
        return (Map<String, String>) textures.get("SKIN").get("metadata");
    }

    public String getSkinModel() {
        Map<String, String> metadata = getSkinMetadata();
        if (metadata == null) return "default";
        return metadata.get("model");
    }
}
