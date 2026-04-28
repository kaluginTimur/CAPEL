package io.github.kalugintimur.capel.dashboard;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;

public class DashboardAggregator {

    public DashboardResponse getDashboard(String userId) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var profile = scope.fork(() -> fetchProfile(userId));
            var billingStatus = scope.fork(() -> fetchBillingStatus(userId));
            var library = scope.fork(() -> fetchLibrary(userId));

            scope.join().throwIfFailed(RuntimeException::new);

            return new DashboardResponse(profile.get(), billingStatus.get(), library.get());
        }
    }

    private String fetchProfile(String userId) throws InterruptedException {
        Thread.sleep(200); // Simulates blocking I/O
        return "User_Profile_Data";
    }

    private String fetchBillingStatus(String userId) throws InterruptedException {
        Thread.sleep(300); // Simulates blocking I/O
        return "Active_Subscription";
    }

    private List<String> fetchLibrary(String userId) throws InterruptedException {
        Thread.sleep(500); // Simulates blocking I/O
        return List.of("S.T.A.L.K.E.R. 2", "Trails in the Sky");
    }
}
