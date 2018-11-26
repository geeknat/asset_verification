package com.sciasv.asv.listeners;

import android.widget.DatePicker;

/**
 * @author Geek Nat
 *         On 6/30/2016.
 */
public interface DatePickerListener {
    void onDatePicked(DatePicker view, String year, String monthOfYear, String dayOfMonth);
}
