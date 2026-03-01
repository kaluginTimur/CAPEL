package io.github.kalugintimur.capel.context;

import java.lang.ScopedValue;

public class UserContextContainer {

    public static final ScopedValue<UserContext> USER_CONTEXT = ScopedValue.newInstance();
}
