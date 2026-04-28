package io.github.kalugintimur.capel.dashboard;

import java.util.List;

public record DashboardResponse(String profile, String billingStatus, List<String> library) {
}
