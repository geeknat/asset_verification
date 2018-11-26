package com.sciasv.asv.listeners;

import android.widget.TimePicker;

/**
 * @author Geek Nat
 *         On 6/30/2016.
 */
public interface TimePickerListener {
    void onTimePicked(TimePicker view, String hourOfDay, String minute);
}
