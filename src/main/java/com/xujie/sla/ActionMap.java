package com.xujie.sla;

import java.util.SortedMap;
import java.util.TreeMap;


public class ActionMap {

    private SortedMap<String, Integer> actionMap;

    private static ActionMap am;

    private ActionMap() {
        actionMap = new TreeMap<String, Integer>();
    }

    public static ActionMap getInstance() {
        if (am == null) {
            am = new ActionMap();
        }
        return am;
    }

    public void addAction(String action) {
        if (!actionMap.containsKey(action)) {
            actionMap.put(action, 1);
        } else {
            actionMap.put(action, actionMap.get(action) + 1);
        }
    }

    public void listActionCount() {
        for (String action : actionMap.keySet()) {
            System.out.println(action + "," + actionMap.get(action));
        }
    }

    public void listAllActions() {

    }

}
