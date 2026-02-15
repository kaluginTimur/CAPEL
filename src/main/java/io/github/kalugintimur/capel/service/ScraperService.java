package io.github.kalugintimur.capel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class ScraperService {

    private final Semaphore rateLimiter = new Semaphore(3);
    private final ReentrantLock resultsLock = new ReentrantLock();

    private final List<String> results = new ArrayList<>();
    private final Random random = new Random();

    public void scrapeGames(List<Integer> gameIds) {
        int totalGames = gameIds.size();

        CountDownLatch latch = new CountDownLatch(totalGames);
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            System.out.println("Starting scrape for " + totalGames + " games...");

            for (int gameId : gameIds) {
                executor.submit(() -> {
                    try {
                        rateLimiter.acquire();
                        try {
                            System.out.println("Scraping Game ID: " + gameId + " (Thread: " + Thread.currentThread().getName() + ")");

                            Thread.sleep(random.nextInt(500) + 100);

                            String result = "Data for Game " + gameId;
                            resultsLock.lock();
                            try {
                                results.add(result);
                            } finally {
                                resultsLock.unlock();
                            }
                        } finally {
                            rateLimiter.release();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Thread interrupted: " + gameId);
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Scraping Thread interrupted");
            return;
        }

        System.out.println("All games scraped. Total results: " + results.size());
    }
}
