package com.sciasv.asv.listeners;

/**
 * @author Geek Nat
 *         On 9/5/2016.
 */
public interface DateRangeListener {
    void onDateRangeSelected(String startDate, String endDate, boolean isSameMonth);

    void onCancel();
}
