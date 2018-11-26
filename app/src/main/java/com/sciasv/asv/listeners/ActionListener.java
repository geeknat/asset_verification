package com.sciasv.asv.listeners;

/**
 * @author Geek Nat
 *         On 9/19/2016.
 */
public interface ActionListener {
    void onActionCancelled(String cancelMessage);

    void onActionCompleted(String completedMessage);
}
