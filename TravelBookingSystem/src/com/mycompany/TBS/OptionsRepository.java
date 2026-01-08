package com.mycompany.TBS;

import java.util.*;

public class OptionsRepository {
    private static Map<String, List<String>> optionsMap = DatabaseManager.loadOptions();

    public static String[] get(String category) {
        List<String> list = optionsMap.getOrDefault(category, new ArrayList<>());
        return list.toArray(new String[0]);
    }

    public static void add(String category, String value) {
        if (!optionsMap.get(category).contains(value)) {
            optionsMap.get(category).add(value);
            DatabaseManager.saveOption(category, value);
        }
    }

    public static void delete(String category, String value) {
        if (optionsMap.get(category).contains(value)) {
            optionsMap.get(category).remove(value);
            DatabaseManager.deleteOption(category, value);
        }
    }
}