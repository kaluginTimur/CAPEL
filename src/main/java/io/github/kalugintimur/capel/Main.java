package io.github.kalugintimur.capel;

import io.github.kalugintimur.capel.service.ScraperService;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        scrapeGames();
    }

    private static void scrapeGames() {
        ScraperService service = new ScraperService();
        List<Integer> games = new ArrayList<>();
        for (int i = 1; i <= 10; i++) games.add(i);

        long start = System.currentTimeMillis();
        service.scrapeGames(games);
        long end = System.currentTimeMillis();

        System.out.println("Total Time: " + (end - start) + "ms");
        // Expected behavior:
        // - Should see groups of ~3 print statements appearing together.
        // - Total results should be 10.
        // - Execution time should be roughly (10 / 3) * avg_latency.
    }
}
