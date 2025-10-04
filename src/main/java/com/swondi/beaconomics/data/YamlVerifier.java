package com.swondi.beaconomics.data;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.YamlManager;
import com.swondi.beaconomics.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


@FunctionalInterface
interface YamlFileChecker {
    boolean check();
}

public class YamlVerifier {

    private static final Map<String, YamlFileChecker> verifiers = new HashMap<>();
    private static final Logger logger = Beaconomics.getInstance().getLogger();

    static {
        verifiers.put("nexuses.yml", YamlVerifier::checkBeaconFileIntegrity);
    }

    public static boolean ensureYamlIntegrity() {

        for (Map.Entry<String, YamlFileChecker> entry : verifiers.entrySet()) {
            YamlFileChecker checker = entry.getValue();
            if (checker.check()) {
                logger.severe("File " + entry.getKey() + " has failed the integrity check.");
                return true;
            }

            logger.info("File " + entry.getKey() + " has been successfully checked.");
        }

        return false;
    }

    private static boolean checkBeaconFileIntegrity() {
        YamlManager yaml = new YamlManager("nexuses.yml");

        if (!yaml.getConfiguration().isConfigurationSection("nexuses")) {
            logger.warning("'nexuses' key missing or invalid. Creating empty section...");
            yaml.getConfiguration().createSection("nexuses");
            yaml.save();
        }

        return false;
    }
}
