package com.redpxnda.tetrutils.client;

import java.util.ArrayList;

public class ClientAchievementData {
    private static ArrayList<String> achievements;

    public static void set(ArrayList<String> achievements) {
        ClientAchievementData.achievements = achievements;
    }

    public static ArrayList<String> getAchievements() {
        return achievements;
    }
}
