package com.sciasv.asv.listeners;

import java.util.Map;

/**
 * @author Geek Nat
 *         On 9/19/2016.
 */
public interface BuildDataListener {
    void onActionCancelled(String cancelMessage);

    void onActionCompleted(Map<String, String> map);
}
