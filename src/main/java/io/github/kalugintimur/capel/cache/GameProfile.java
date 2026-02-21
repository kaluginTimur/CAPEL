package io.github.kalugintimur.capel.cache;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record GameProfile(UUID gameId, Map<String, String> payload) {

    public GameProfile {
        Objects.requireNonNull(payload);
        payload = Map.copyOf(payload);
    }
}
