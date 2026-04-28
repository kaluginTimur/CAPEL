package io.github.kalugintimur.capel.telemetry.utils;

import io.github.kalugintimur.capel.telemetry.TelemetryEvent;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

public class TelemetryBatchSpliterator implements Spliterator<TelemetryEvent> {

    private static final int RECORD_SIZE = 64;

    private final byte[] payload;
    private int index;
    private final int fence;

    public TelemetryBatchSpliterator(byte[] payload) {
        this(payload, 0, payload.length);
    }

    public TelemetryBatchSpliterator(byte[] payload, int index, int fence) {
        this.payload = payload;
        this.index = index;
        this.fence = fence;
    }

    @Override
    public boolean tryAdvance(Consumer<? super TelemetryEvent> action) {
        Objects.requireNonNull(action);
        if (index >= 0 && index < fence) {
            action.accept(new TelemetryEvent(payload, index));
            index += RECORD_SIZE;
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<TelemetryEvent> trySplit() {
        int lo = index, mid = ((lo + fence) >>> 1) & -RECORD_SIZE;
        if (lo >= mid) {
            return null;
        }
        return new TelemetryBatchSpliterator(payload, lo, index = mid);
    }

    @Override
    public long estimateSize() {
        return (long) (fence - index) / RECORD_SIZE;
    }

    @Override
    public int characteristics() {
        return SIZED | SUBSIZED | IMMUTABLE | ORDERED | NONNULL;
    }
}
