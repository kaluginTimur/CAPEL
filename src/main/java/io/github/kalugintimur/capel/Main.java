package io.github.kalugintimur.capel;

import io.github.kalugintimur.capel.queue.BatchQueue;
import io.github.kalugintimur.capel.service.ScraperService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        batchQueueCase();
    }

    private static void scrapeGames() {
        ScraperService service = new ScraperService();
        List<Integer> games = new ArrayList<>();
        for (int i = 1; i <= 10; i++) games.add(i);

        long start = System.currentTimeMillis();
        service.scrapeGames(games);
        long end = System.currentTimeMillis();

        System.out.println("Total Time: " + (end - start) + "ms");
    }

    private static void batchQueueCase() {
        BatchQueue queue = new BatchQueue();
        int batchSize = 3;

        Thread processor = new Thread(() -> {
            System.out.println("Processor: Waiting for batch of " + batchSize + "...");
            try {
                List<String> batch = queue.takeBatch(batchSize);
                System.out.println("Processor: Got batch! " + batch);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        processor.start();

        ExecutorService scrapers = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            int id = i;
            scrapers.submit(() -> {
                try {
                    Thread.sleep(200 * id);
                    System.out.println("Scraper: Adding item " + id);
                    queue.add("URL-" + id);
                } catch (InterruptedException e) { }
            });
        }

        for (int i = 0; i < 5; i++) {
            System.out.println("Stats (Optimistic): Total Processed = " + queue.getTotalProcessed());
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        scrapers.shutdown();
        try {
            processor.join(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Final Stats: Total Processed = " + queue.getTotalProcessed());
    }
}
