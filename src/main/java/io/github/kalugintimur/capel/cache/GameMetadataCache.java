package io.github.kalugintimur.capel.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GameMetadataCache {

    private final ConcurrentMap<UUID, GameProfile> cache = new ConcurrentHashMap<>();

    public void ingestPayload(UUID gameId, String source, String rawData) {
        Objects.requireNonNull(gameId);
        Objects.requireNonNull(source);
        Objects.requireNonNull(rawData);
        cache.compute(gameId, (id, profile) -> {
            Map<String, String> payload;
            if (profile == null) {
                payload = new HashMap<>();
            } else {
                payload = new HashMap<>(profile.payload());
            }
            payload.put(source, rawData);
            return new GameProfile(gameId, payload);
        });
    }
}
