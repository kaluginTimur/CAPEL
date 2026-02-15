package io.github.kalugintimur.capel.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

public class BatchQueue {

    private final List<String> queue = new ArrayList<>();
    private final ReentrantLock queueLock = new ReentrantLock();
    private final Condition enoughDataCondition = queueLock.newCondition();

    private long totalProcessed = 0;
    private final StampedLock statsLock = new StampedLock();

    public void add(String url) {
        queueLock.lock();
        try {
            queue.add(url);
            enoughDataCondition.signalAll();
        } finally {
            queueLock.unlock();
        }
    }

    public List<String> takeBatch(int batchSize) throws InterruptedException {
        List<String> batch = new ArrayList<>(batchSize);

        queueLock.lock();
        try {
            while (queue.size() < batchSize) {
                enoughDataCondition.await();
            }

            List<String> urls = queue.subList(0, batchSize);
            batch.addAll(urls);
            urls.clear();
        } finally {
            queueLock.unlock();
        }

        long writeLock = statsLock.writeLock();
        try {
            totalProcessed += batch.size();
        } finally {
            statsLock.unlock(writeLock);
        }
        return batch;
    }

    public long getTotalProcessed() {
        long readLock = statsLock.tryOptimisticRead();
        long totalProcessed = this.totalProcessed;
        if (!statsLock.validate(readLock)) {
            readLock = statsLock.readLock();
            try {
                totalProcessed = this.totalProcessed;
            } finally {
                statsLock.unlock(readLock);
            }
        }
        return totalProcessed;
    }
}
