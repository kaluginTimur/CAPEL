package io.github.kalugintimur.capel.telemetry;

public record TelemetryEvent(byte[] payload, int offset) {

    public int getPlayerCount() {
        return 1;
    }
}
