package com.palight.playerinfo.animations;

import com.palight.playerinfo.animations.emotes.Emote;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EmoteHandler {
    public static Map<UUID, Emote> playingEmotes = new HashMap<>();
    public static Map<UUID, Long> startPlayingTime = new HashMap<>();

    public static void startPlayingEmote(UUID uuid, Emote emote) {
        playingEmotes.put(uuid, emote);
        startPlayingTime.put(uuid, System.currentTimeMillis());
    }

    public static void stopPlayingEmote(UUID uuid) {
        playingEmotes.remove(uuid);
        startPlayingTime.remove(uuid);
    }
}
