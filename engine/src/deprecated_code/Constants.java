/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated_code;

/**
 *
 * @author juehv
 */
public final class Constants {

    private Constants() {
    }

    // Prefs
    public static final String CARELINK_CSV_FILE_LAST_PATH = "carelinkLastPath";
    public static final String CARELINK_USER_KEY = "carelinkUser";
    public static final String CARELINK_PW_KEY = "carelinkPw";
    public static final String GOOGLE_USER_KEY = "googleUser";
    public static final String GOOGLE_PW_KEY = "googlePw";
//    public static final String BG_UNIT_KEY = "bgUnit";
//    public static final String BG_UNIT_MG = "mg";
//    public static final String BG_UNIT_MMOL = "mmol";
//    public static final String BG_UNIT_DEFAULT = BG_UNIT_MG;
    public static final String HYPO_THRESHOLD_MG_KEY = "hypoMgThreshold";
    public static final String HYPO_THRESHOLD_MMOL_KEY = "hypoMmolThreshold";
    public static final double HYPO_THRESHOLD_MG_DEFAULT = 71;
    public static final double HYPO_THRESHOLD_MMOL_DEFAULT = 3.9;
    public static final String HYPO_FOLLOW_TIME_KEY = "hypoFollowingTime";
    public static final int HYPO_FOLLOW_TIME_DEFAULT = 60;
    public static final String HYPER_FOLLOW_TIME_KEY = "hyperFollowingTime";
    public static final int HYPER_FOLLOW_TIME_DEFAULT = 1440;
    public static final String HYPER_THRESHOLD_MG_KEY = "hyperMgThreshold";
    public static final String HYPER_THRESHOLD_MMOL_KEY = "hyperMmolThreshold";
    public static final double HYPER_THRESHOLD_MG_DEFAULT = 180;
    public static final double HYPER_THRESHOLD_MMOL_DEFAULT = 16.7;
    public static final String HYPO_EXERCISE_HISTORY_TIME_KEY = "hypoExerciseHistoryTime";
    public static final int HYPO_EXERCISE_HISTORY_TIME_DEFAULT = 180;
    public static final String HYPO_MEAL_HISTORY_TIME_KEY = "hypoFoodHistoryTime";
    public static final int HYPO_MEAL_HISTORY_TIME_DEFAULT = 720;
    public static final String HYPER_MEAL_HISTORY_TIME_KEY = "hyperFoodHistoryTime";
    public static final int HYPER_MEAL_HISTORY_TIME_DEFAULT = 720;

    public static final String SLEEP_INDICATION_BED_TIME_KEY = "sleepIndicationStartTime";
    public static final int SLEEP_INDICATION_BED_TIME_DEFAULT = 22;
    public static final String SLEEP_INDICATION_WAKEUP_TIME_KEY = "sleepIndicationStopTime";
    public static final int SLEEP_INDICATION_WAKEUP_TIME_DEFAULT = 6;
    public static final String SLEEP_INDICATION_THRESHOLD_KEY = "sleepIndicationThreshold";
    public static final int SLEEP_INDICATION_THRESHOLD_DEFAULT = 30;

    public static final double MG_TO_MMOL_FACTOR = 0.0555;
    public static final double MMOL_TO_MG_FACTOR = 18.0182;

    // Carelink Parsing
    public static final int CARELINK_CSV_EMPTY_LINES = 11;
    public static int CARELINK_CSV_LANG_SELECTION = 0;
    public static final String[][] CARELINK_CSV_HEADER = {
        {"Datum", "Zeit", "Roh-Typ", "Roh-Werte"}, //GER
        {"Date", "Time", "Roh-Typ", "Roh-Werte"} // ENG
    };
    public static final String[] CARELINK_CSV_DATETIME_FORMAT = {
        "HH:mm:ssdd.MM.yy", "HH:mm:ssdd.MM.yy"
    };
    public static final String[] CARELINK_TYPE
            = {"Rewind", "Prime", "JournalEntryExerciseMarker",
                "BGCapturedOnPump", "BGReceived",
                "BolusWizardBolusEstimate", "BolusNormal", "BasalProfileStart"};
    public static final String CARELINK_RAW_VALUE_AMOUNT = "AMOUNT";
    public static final String CARELINK_RAW_VALUE_CARB_INPUT = "CARB_INPUT";
    public static final String CARELINK_RAW_VALUE_BG_LINK_ID = "PARADIGM_LINK_ID";

    public static final String[] GOOGLE_TYPE
            = {"Walk", "Bicycle", "Run"};

    public static final String[] LIBRE_TYPE
            = {"ScanGlucose", "HistoricGlucose", "BloodGlucose", "TimeChanged"};
    public static final int[] LIBRE_TYPE_INTEGER
            = {1, 0, 2, 6};
    public static final String[][] LIBRE_CSV_HEADER = {
        {"Uhrzeit", "Art des Eintrags", "Historische Glukose (mg/dL)",
            "Gescannte Glukose (mg/dL)", "Teststreifen-Blutzucker (mg/dL)"} //GER
    };

    
    // Other
    public static final String DATE_TIME_OUTPUT_FORMAT = "dd.MM. HH:mm";

    // could be interesting
//    private static final Map<String, String> CARELINK_TYPE;
//    static {
//        Map<String, String> aMap = new HashMap<>();
//        aMap.put("BG_RECEIVE", "one");
//        aMap.put("BG_RECEIVE", "two");
//        CARELINK_TYPE = Collections.unmodifiableMap(aMap);
//    }
}
