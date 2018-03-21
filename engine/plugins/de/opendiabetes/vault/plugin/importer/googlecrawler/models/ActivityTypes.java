package de.opendiabetes.vault.plugin.importer.googlecrawler.models;

/**
 * ActivityTypes model.
 */
public final class ActivityTypes {

    public static final int IN_VEHICLE = 0;
    public static final int BIKING = 1;
    public static final int ON_FOOT = 2;
    public static final int STILL = 3;
    public static final int UNKNOWN = 4;
    public static final int TILTING = 5;
    public static final int WALKING = 7;
    public static final int RUNNING = 8;
    public static final int AEROBICS = 9;
    public static final int BADMINTON = 10;
    public static final int BASEBALL = 11;
    public static final int BASKETBALL = 12;
    public static final int BIATHLON = 13;
    public static final int HANDBIKING = 14;
    public static final int MOUNTAINBIKING = 15;
    public static final int ROAD_BIKING = 16;
    public static final int SPINNING = 17;
    public static final int STATIONARY_BIKING = 18;
    public static final int UTILITY_BIKING = 19;
    public static final int BOXING = 20;
    public static final int CALISTHENICS = 21;
    public static final int CIRCUIT_TRAINING = 22;
    public static final int CRICKET = 23;
    public static final int DANCING = 24;
    public static final int ELLIPTICAL = 25;
    public static final int FENCING = 26;
    public static final int AMERICAN_FOOTBALL = 27;
    public static final int AUSTRALIAN_FOOTBALL = 28;
    public static final int SOCCER = 29;
    public static final int FRISBEE = 30;
    public static final int GARDENING = 31;
    public static final int GOLF = 32;
    public static final int GYMNASTICS = 33;
    public static final int HANDBALL = 34;
    public static final int HIKING = 35;
    public static final int HOCKEY = 36;
    public static final int HORSEBACK_RIDING = 37;
    public static final int HOUSEWORK = 38;
    public static final int JUMPING_ROPE = 39;
    public static final int KAYAKING = 40;
    public static final int KETTLEBELL_TRAINING = 41;
    public static final int KICKBOXING = 42;
    public static final int KITESURFING = 43;
    public static final int MARTIAL_ARTS = 44;
    public static final int MEDITATION = 45;
    public static final int MIXED_MARTIAL_ARTS = 46;
    public static final int P90X_EXERCISES = 47;
    public static final int PARAGLIDING = 48;
    public static final int PILATES = 49;
    public static final int POLO = 50;
    public static final int RACQUETBALL = 51;
    public static final int ROCK_CLIMBING = 52;
    public static final int ROWING = 53;
    public static final int ROWING_MACHINE = 54;
    public static final int RUGBY = 55;
    public static final int JOGGING = 56;
    public static final int RUNNING_ON_SAND = 57;
    public static final int RUNNING_ON_TREADMILL = 58;
    public static final int SAILING = 59;
    public static final int SCUBA_DIVING = 60;
    public static final int SKATEBOARDING = 61;
    public static final int SKATING = 62;
    public static final int CROSS_SKATING = 63;
    public static final int INLINE_SKATING = 64;
    public static final int SKIING = 65;
    public static final int BACKCOUNTRY_SKIING = 66;
    public static final int CROSSCOUNTRY_SKIING = 67;
    public static final int DOWNHILL_SKIING = 68;
    public static final int KITE_SKIING = 69;
    public static final int ROLLER_SKIING = 70;
    public static final int SLEDDING = 71;
    public static final int SLEEPING = 72;
    public static final int SNOWBOARDING = 73;
    public static final int SNOWMOBILE = 74;
    public static final int SNOWSHOEING = 75;
    public static final int SQUASH = 76;
    public static final int STAIR_CLIMBING = 77;
    public static final int STAIR_CLIMBING_MACHINE = 78;
    public static final int STANDUP_PADDLEBOARDING = 79;
    public static final int STRENGTH_TRAINING = 80;
    public static final int SURFING = 81;
    public static final int SWIMMING = 82;
    public static final int SWIMMING_SWIMMING_POOL = 83;
    public static final int SWIMMING_OPEN_WATER = 84;
    public static final int TABLE_TENNIS = 85;
    public static final int TEAM_SPORTS = 86;
    public static final int TENNIS = 87;
    public static final int TREADMILL = 88;
    public static final int VOLLEYBALL = 89;
    public static final int VOLLEYBALL_BEACH = 90;
    public static final int VOLLEYBALL_INDOOR = 91;
    public static final int WAKEBOARDING = 92;
    public static final int WALKING_FITNESS = 93;
    public static final int NORDING_WALKING = 94;
    public static final int WALKING_TREADMILL = 95;
    public static final int WATERPOLO = 96;
    public static final int WEIGHTLIFTING = 97;
    public static final int WHEELCHAIR = 98;
    public static final int WINDSURFING = 99;
    public static final int YOGA = 100;
    public static final int ZUMBA = 101;
    public static final int DIVING = 102;
    public static final int ERGOMETER = 103;
    public static final int ICE_SKATING = 104;
    public static final int INDOOR_SKATING = 105;
    public static final int CURLING = 106;
    public static final int OTHER_UNCLASSIFIED = 108;
    public static final int LIGHT_SLEEP = 109;
    public static final int DEEP_SLEEP = 110;
    public static final int REM_SLEEP = 111;
    public static final int AWAKE_DURING_SLEEP = 112;
    public static final int CROSSFIT = 113;
    public static final int HIIT = 114;
    public static final int INTERVAL_TRAINING = 115;
    public static final int WALKING_STROLLER = 116;
    public static final int ELEVATOR = 117;
    public static final int ESCALATOR = 118;
    public static final int ARCHERY = 119;
    public static final int SOFTBALL = 120;

    /**
     * Singleton instance.
     */
    private static ActivityTypes instance;

    /**
     * Constructor.
     */
    private ActivityTypes() {
    }

    /**
     * Returns the singleton instance.
     * @return singleton instance of the model
     */
    public static ActivityTypes getInstance() {
        if (ActivityTypes.instance == null) {
            ActivityTypes.instance = new ActivityTypes();
        }
        return ActivityTypes.instance;
    }

    /**
     * Returns the human readable representation of the given activity identifier index.
     * @param activity an activity identifier index
     * @return the human readable string of the activity type, otherwise "Unknown"
     */
    public String getReadableActivityType(final int activity) {
        switch (activity) {
            case IN_VEHICLE:
                return "In vehicle";
            case BIKING:
                return "Biking";
            case ON_FOOT:
                return "On foot";
            case STILL:
                return "Still";
            case UNKNOWN:
                return "Unknown";
            case TILTING:
                return "Tilting";
            case WALKING:
                return "Walking";
            case RUNNING:
                return "Running";
            case AEROBICS:
                return "Aerobics";
            case BADMINTON:
                return "Badminton";
            case BASEBALL:
                return "Baseball";
            case BASKETBALL:
                return "Basketball";
            case BIATHLON:
                return "Biathlon";
            case HANDBIKING:
                return "Handbiking";
            case MOUNTAINBIKING:
                return "Mountainbiking";
            case ROAD_BIKING:
                return "Road biking";
            case SPINNING:
                return "Spinning";
            case STATIONARY_BIKING:
                return "Stationary biking";
            case UTILITY_BIKING:
                return "Utility biking";
            case BOXING:
                return "Boxing";
            case CALISTHENICS:
                return "Calisthenics";
            case CIRCUIT_TRAINING:
                return "Circuit training";
            case CRICKET:
                return "Cricket";
            case DANCING:
                return "Dancing";
            case ELLIPTICAL:
                return "Elliptical";
            case FENCING:
                return "Fencing";
            case AMERICAN_FOOTBALL:
                return "American Football";
            case AUSTRALIAN_FOOTBALL:
                return "Australian Football";
            case SOCCER:
                return "Football (Soccer)";
            case FRISBEE:
                return "Frisbee";
            case GARDENING:
                return "Gardening";
            case GOLF:
                return "Golf";
            case GYMNASTICS:
                return "Gymnastics";
            case HANDBALL:
                return "Handball";
            case HIKING:
                return "Hiking";
            case HOCKEY:
                return "Hockey";
            case HORSEBACK_RIDING:
                return "Horseback riding";
            case HOUSEWORK:
                return "Housework";
            case JUMPING_ROPE:
                return "Jumping rope";
            case KAYAKING:
                return "Kayaking";
            case KETTLEBELL_TRAINING:
                return "Kettlebell training";
            case KICKBOXING:
                return "Kickboxing";
            case KITESURFING:
                return "Kitesurfing";
            case MARTIAL_ARTS:
                return "Martial arts";
            case MEDITATION:
                return "Meditation";
            case MIXED_MARTIAL_ARTS:
                return "Mixed martial arts";
            case P90X_EXERCISES:
                return "P90X exercises";
            case PARAGLIDING:
                return "Paragliding";
            case PILATES:
                return "Pilates";
            case POLO:
                return "Polo";
            case RACQUETBALL:
                return "Racquetball";
            case ROCK_CLIMBING:
                return "Rock climbing";
            case ROWING:
                return "Rowing";
            case ROWING_MACHINE:
                return "Rowing machine";
            case RUGBY:
                return "Rugby";
            case JOGGING:
                return "Jogging";
            case RUNNING_ON_SAND:
                return "Running on sand";
            case RUNNING_ON_TREADMILL:
                return "Running (treadmill)";
            case SAILING:
                return "Sailing";
            case SCUBA_DIVING:
                return "Scuba diving";
            case SKATEBOARDING:
                return "Skateboarding";
            case SKATING:
                return "Skating";
            case CROSS_SKATING:
                return "Cross skating";
            case INLINE_SKATING:
                return "Inline skating (rollerblading)";
            case SKIING:
                return "Skiing";
            case BACKCOUNTRY_SKIING:
                return "Back-country skiing";
            case CROSSCOUNTRY_SKIING:
                return "Cross-country skiing";
            case DOWNHILL_SKIING:
                return "Downhill skiing";
            case KITE_SKIING:
                return "Kite skiing";
            case ROLLER_SKIING:
                return "Roller skiing";
            case SLEDDING:
                return "Sledding";
            case SLEEPING:
                return "Sleeping";
            case SNOWBOARDING:
                return "Snowboarding";
            case SNOWMOBILE:
                return "Snowmobile";
            case SNOWSHOEING:
                return "Snowshoeing";
            case SQUASH:
                return "Squash";
            case STAIR_CLIMBING:
                return "Stair climbing";
            case STAIR_CLIMBING_MACHINE:
                return "Stair-climbing machine";
            case STANDUP_PADDLEBOARDING:
                return "Stand-up paddleboarding";
            case STRENGTH_TRAINING:
                return "Strength training";
            case SURFING:
                return "Surfing";
            case SWIMMING:
                return "Swimming";
            case SWIMMING_SWIMMING_POOL:
                return "Swimming (swimming pool)";
            case SWIMMING_OPEN_WATER:
                return "Swimming (open water)";
            case TABLE_TENNIS:
                return "Table tennis (ping pong)";
            case TEAM_SPORTS:
                return "Team sports";
            case TENNIS:
                return "Tennis";
            case TREADMILL:
                return "Treadmill (walking or running)";
            case VOLLEYBALL:
                return "Volleyball";
            case VOLLEYBALL_BEACH:
                return "Volleyball (beach)";
            case VOLLEYBALL_INDOOR:
                return "Volleyball (indoor)";
            case WAKEBOARDING:
                return "Wakeboarding";
            case WALKING_FITNESS:
                return "Walking (fitness)";
            case NORDING_WALKING:
                return "Nording walking";
            case WALKING_TREADMILL:
                return "Walking (treadmill)";
            case WATERPOLO:
                return "Waterpolo";
            case WEIGHTLIFTING:
                return "Weightlifting";
            case WHEELCHAIR:
                return "Wheelchair";
            case WINDSURFING:
                return "Windsurfing";
            case YOGA:
                return "Yoga";
            case ZUMBA:
                return "Zumba";
            case DIVING:
                return "Diving";
            case ERGOMETER:
                return "Ergometer";
            case ICE_SKATING:
                return "Ice skating";
            case INDOOR_SKATING:
                return "Indoor skating";
            case CURLING:
                return "Curling";
            case OTHER_UNCLASSIFIED:
                return "Other (unclassified fitness activity)";
            case LIGHT_SLEEP:
                return "Light sleep";
            case DEEP_SLEEP:
                return "Deep sleep";
            case REM_SLEEP:
                return "REM sleep";
            case AWAKE_DURING_SLEEP:
                return "Awake (during sleep cycle)";
            case CROSSFIT:
                return "Crossfit";
            case HIIT:
                return "HIIT";
            case INTERVAL_TRAINING:
                return "Interval Training";
            case WALKING_STROLLER:
                return "Walking (stroller)";
            case ELEVATOR:
                return "Elevator";
            case ESCALATOR:
                return "Escalator";
            case ARCHERY:
                return "Archery";
            case SOFTBALL:
                return "Softball";
            default:
                return "Unknown";
        }
    }
}

