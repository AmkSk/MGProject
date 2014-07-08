package com.mg.androidapp.common;

public class Constants {

    // =============================================================================
    // Property list files
    // =============================================================================

    public static String ACTUALITIES = "actuals.plist";
    public static String BUILDINGS = "buildings.plist";
    public static String LAST_CHANGE = "lastChange.plist";
    public static String EVENTS = "events.plist";
    public static String EXHIBITIONS = "exhibitions.plist";
    public static String CONSTANT_EXHIBITIONS = "constantExhibitions.plist";

    // =============================================================================
    // Animation times
    // =============================================================================

    public static int FADEIN = 1500;
    public static int FADEOUT = 1500;
    public static int ANIMATION_OFFSET = 2000;

    // =============================================================================
    // ListView object distinguishing enum
    // =============================================================================

    public enum dataType{
        BUILDING,
        ACTUALITY,
        EXHIBITION
    }
}
