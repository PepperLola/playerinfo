package com.palight.playerinfo.data;

import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.base64.Base64Encoder;

import javax.xml.bind.DatatypeConverter;
import java.util.Map;

import static com.palight.playerinfo.PlayerInfo.gson;

public class PlayerProperties {
    private long timestamp;
    private String profileId;
    private String profileName;
    private Map<String, Map<String, ?>> textures;

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

    public Map<String, Map<String, ?>> getTextures() {
        return textures;
    }

    public void setTextures(Map<String, Map<String, ?>> textures) {
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

    public String toString() {
        return gson.toJson(this);
    }

    public String toBase64String() {
        return DatatypeConverter.printBase64Binary(this.toString().getBytes());
    }

    public static PlayerProperties fromBase64(String base64Encoded) {
        String decodedBase64 = new String(DatatypeConverter.parseBase64Binary(base64Encoded));
        return gson.fromJson(decodedBase64, PlayerProperties.class);
    }
}
