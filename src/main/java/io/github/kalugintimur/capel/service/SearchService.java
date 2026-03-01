package io.github.kalugintimur.capel.service;

import io.github.kalugintimur.capel.context.UserContext;
import io.github.kalugintimur.capel.context.UserContextContainer;

public class SearchService {

    public String search(String query) {
        if (UserContextContainer.USER_CONTEXT.isBound()) {
            UserContext userContext = UserContextContainer.USER_CONTEXT.get();
            String tier = userContext.subscriptionTier();
            if ("premium".equals(tier)) {
                System.out.printf("Premium tier search for %s", userContext.userName());
                return "premium result";
            }
            System.out.printf("Free tier search for %s", userContext.userName());
            return "free result";
        } else {
            throw new IllegalStateException("UserContext not bound");
        }
    }
}
