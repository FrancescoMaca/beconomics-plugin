package com.swondi.beaconomics.helpers;

import java.util.ArrayList;
import java.util.List;

public class CommandHelper {

    public static List<String> getSuggestions(String input, List<String> cmds) {
        List<String> suggestions = new ArrayList<>();
        for (String sub : cmds) {
            if (sub.toLowerCase().startsWith(input.toLowerCase())) {
                suggestions.add(sub);
            }
        }

        return suggestions;
    }
}
