package io.github.kalugintimur.capel.controller;

import io.github.kalugintimur.capel.context.UserContext;
import io.github.kalugintimur.capel.context.UserContextContainer;
import io.github.kalugintimur.capel.service.SearchService;

public class SearchController {

    private final SearchService searchService;

    public SearchController() {
        this.searchService = new SearchService();
    }

    // search request handle simulation
    public String search(String[] request) {
        String userName = request[0];
        String subscriptionTier = request[1];
        String query = request[2];
        UserContext userContext = new UserContext(userName, subscriptionTier);
        return ScopedValue.getWhere(UserContextContainer.USER_CONTEXT, userContext, () -> searchService.search(query));
    }
}
