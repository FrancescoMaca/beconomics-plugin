package com.swondi.beaconomics.cli.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenCommandManager {
    private static final Map<UUID, String> confirmationTokens =  new HashMap<>();

    public static String createConfirmationToken(UUID sender) {
        String token = UUID.randomUUID().toString();
        confirmationTokens.put(sender, token);
        return token;
    }

    /** Validate and consume a one-time token */
    public static boolean isConfirmationTokenValid(UUID sender, String confirmationToken) {
        String stored = confirmationTokens.remove(sender);
        return stored != null && stored.equals(confirmationToken);
    }

    /** Force-remove a token without validating */
    public static void voidTokenConfirmation(UUID sender) {
        confirmationTokens.remove(sender);
    }
}
