package edu.hm.stundenplan.entities;

/**
 * Used to describe day of week.
 * Starting with Monday == 1 we avoid index calculations when mapping to tables with row and column headers at index 0.
 * @author ab@cs.hm.edu
 */
public enum WeekDay {
    NONE, // out of plan!
    Montag,
    Dienstag,
    Mittwoch,
    Donnerstag,
    Freitag,
    Samstag,
    Sonntag,
}
