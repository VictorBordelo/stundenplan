package edu.hm.stundenplan.entities;

/**
 * Used to identify a slot per day.
 * Starting with Monday == 1 we avoid index calculations when mapping to tables with row and column headers at index 0.
 * See also enum WeekDay
 * @author ab@cs.hm.edu
 *
 */
public enum TimeSlot {
    NONE,
    Slot1,
    Slot2,
    Slot3,
    Slot4,
    Slot5,
    Slot6,
    Slot7,
}
