/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated_code;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author mswin
 */
public class DataHelper {

    public static boolean dataEntryListContains(List<RawDataEntry> entryList, RawDataEntry entry) {
        for (RawDataEntry tmp : entryList) {
            if (tmp.isEquivalent(entry)) {
                return true;
            }
        }
        return false;
    }

    public static int minutesDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }
        return Math.abs((int) ((laterDate.getTime() / 60000) - (earlierDate.getTime() / 60000)));
    }

    // ###################################################################
    // List computations
    // ###################################################################
    public static List<RawDataEntry> createCleanBgList(List<RawDataEntry> data) {
        List<RawDataEntry> bgList = new ArrayList<>();

        for (RawDataEntry item : data) {
            // stronges is rf bg from measureing device
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[4])) {
                bgList.add(item);
            }
        }

        for (RawDataEntry item : data) {
            // weaker is user entered values
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])) {
                if (!dataEntryListContains(bgList, item)) {
                    bgList.add(item);
                }
            }
        }
        bgList.sort(RawDataEntry.getTimeSortComparator());
        return bgList;
    }

    public static List<RawDataEntry> createHypoList(List<RawDataEntry> cleanBgList,
            double threshold, int hypoTimeRange) {
        List<RawDataEntry> listEntrys = new ArrayList<>();
        RawDataEntry lastItem = null;

        for (RawDataEntry item : cleanBgList) {
            if (item.amount < threshold && item.amount > 0) {
                // check if the item does belong to the same hypo
                if (lastItem == null
                        || minutesDiff(lastItem.timestamp,
                                item.timestamp) > hypoTimeRange) {
                    lastItem = item;
                    listEntrys.add(item);
                }
            } else if (item.amount > 0) {
                // item that is over the threshold --> disconinues the series
                lastItem = null;
            }
        }

        // second pass to exclude
        return listEntrys;
    }

    public static List<RawDataEntry> createHyperList(List<RawDataEntry> cleanBgList,
            double threshold, int hyperTimeRange) {
        List<RawDataEntry> listEntrys = new ArrayList<>();

        for (RawDataEntry item : cleanBgList) {
            if (item.amount > threshold && item.amount > 0) {
                listEntrys.add(item);
            }
        }

        return listEntrys;
    }

    public static List<RawDataEntry> createExerciseMarkerList(List<RawDataEntry> entryList) {
        List<RawDataEntry> listEntrys = new ArrayList<>();

        for (RawDataEntry item : entryList) {
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[2])) {
                listEntrys.add(item);
            }
        }

        return listEntrys;
    }

    public static List<RawDataEntry> createCleanPrimeList(List<RawDataEntry> data) {
        List<RawDataEntry> primeList = new ArrayList<>();

        for (RawDataEntry item : data) {
            // strongest indicator for prime is rewind
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])) {
                primeList.add(item);
                // todo find corresbonding prime, and add amount
            }
        }

        //TODO check is rewind has an prime bevore, if yes, don't add this
        for (RawDataEntry item : data) {
            // a little less strong is prime
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[1])) {
                if (!dataEntryListContains(primeList, item)) {
                    primeList.add(item);
                }
            }
        }

        primeList.sort(RawDataEntry.getTimeSortComparator());
        return primeList;
    }

    public static List<RawDataEntry> filterFollowingHypoValues(List<RawDataEntry> cleanBgList,
            Date startTime, int minuteRange, double threshold) {
        List<RawDataEntry> listEntrys = new ArrayList<>();

        for (RawDataEntry item : cleanBgList) {
            if (startTime.before(item.timestamp)
                    && minutesDiff(item.timestamp, startTime) < minuteRange) {
                listEntrys.add(item);
                if (item.amount > threshold) {
                    // completes the series
                    break;
                }
            }
        }

        return listEntrys;
    }

    public static List<RawDataEntry> filterFollowingHyperValues(List<RawDataEntry> cleanBgList,
            Date startTime, int minuteRange, double threshold) {
        List<RawDataEntry> listEntrys = new ArrayList<>();

        for (RawDataEntry item : cleanBgList) {
            if (startTime.before(item.timestamp)
                    && minutesDiff(item.timestamp, startTime) < minuteRange) {
                listEntrys.add(item);
                if (item.amount < threshold) {
                    // completes the series
                    break;
                }
            }
        }

        return listEntrys;
    }

    public static List<RawDataEntry> filterHistoryValues(List<RawDataEntry> entryList,
            Date startTime, int minuteRange) {
        List<RawDataEntry> listEntrys = new ArrayList<>();

        for (RawDataEntry item : entryList) {
            if (startTime.after(item.timestamp)
                    && minutesDiff(item.timestamp, startTime) < minuteRange) {
                listEntrys.add(item);
            }
        }

        return listEntrys;
    }

    public static RawDataEntry filterNextValue(List<RawDataEntry> cleanBgList,
            Date startTime) {

        for (RawDataEntry item : cleanBgList) {
            if (startTime.before(item.timestamp)) {
                return item;
            }
        }

        return null;
    }

    public static RawDataEntry filterLastValue(List<RawDataEntry> entryList,
            Date startTime) {

        for (int i = entryList.size() - 1; i >= 0; i--) {
            RawDataEntry item = entryList.get(i);
            if (startTime.after(item.timestamp)) {
                return item;
            }
        }

        return null;
    }

    public static boolean isUnitMmol(List<RawDataEntry> cleanBgList) {
        double avg = 0;
        for (RawDataEntry item : cleanBgList) {
            avg += item.amount;
        }
        return (avg / cleanBgList.size()) < 50;
    }

    // this list is needed to create a combination of ke and bolus. --> later
//    public static List<DataEntry> createCleanBolusList(List<DataEntry> data) {
//        List<DataEntry> bolusList = new ArrayList<>();
//
//        for (DataEntry item : data) {
//            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[6])) {
//                bolusList.add(item);
//            }
//        }
//
//        bolusList.sort(DataEntry.getTimeSortComparator());
//        return bolusList;
//    }
    public static List<RawDataEntry> createCleanFoodBolusList(List<RawDataEntry> data) {
        List<RawDataEntry> fullBolusList = new ArrayList<>();
        List<RawDataEntry> bolusList = new ArrayList<>();

        for (RawDataEntry item : data) {
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[5])) {
                fullBolusList.add(item);
            }
        }

        // fill up boli without wizard (uuhhh bad guys)
        for (RawDataEntry item : data) {
            if (item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[6])) {
                if (!dataEntryListContains(fullBolusList, item)) {
                    if (item.amount > 0.0) {
                        fullBolusList.add(item);
                    }
                }
            }
        }

        //remove correction entrys (needed to exclude boli)
        for (RawDataEntry item : fullBolusList) {
            if (item.amount > 0.0) {
                bolusList.add(item);
            }
        }

        bolusList.sort(RawDataEntry.getTimeSortComparator());
        return bolusList;
    }

    public static List<RawDataEntry> filterTimeRange(List<RawDataEntry> list,
            Date fromRagen, Date toRange) {
        //TODO implement
        return list;
    }

    // ###################################################################
    // GUI Helper
    // ###################################################################
    public static String[] createGuiList(List<RawDataEntry> cleanList) {
        List<String> listEntrys = new ArrayList<>();

        for (RawDataEntry item : cleanList) {
            listEntrys.add(item.toGuiListEntry());
        }

        return listEntrys.toArray(new String[]{});
    }

    // ###################################################################
    // Export Helper
    // ###################################################################
    public static String createInformationMailBody(List<RawDataEntry> completeList,
            List<RawDataEntry> primeList, List<RawDataEntry> bolusWizardList, List<RawDataEntry> hypoList,
            List<RawDataEntry> hyperList, List<RawDataEntry> exerciseMarkerList,
            double hypoThreshold, int hypoMealHistoryTime, int exerciseHistoryTime,
            int wakupTime, int bedTime, int sleepThreshold, double hyperThreshold,
            int hyperMealHistoryTime) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat dformat = new SimpleDateFormat(Constants.DATE_TIME_OUTPUT_FORMAT);
        // peamble
        sb.append("\n\n\n\n");

        // create Prime text
        sb.append("Infusionsset-Wechsel:\n");
        for (RawDataEntry item : primeList) {
            sb.append(dformat.format(item.timestamp)).append(" routinemäßig\n");
        }

        // create hypo text
        sb.append("\n\nHypoglykämien (<").append(hypoThreshold).append("):\n");
        for (RawDataEntry item : hypoList) {
            sb.append(item.toTextListEntry())
                    .append("\n");
            sb.append("Gabe es Symptome? Ja.\n");
            sb.append("Konnte die Unterzuckerung selbst behandelt werden? Ja.\n");
            // calc last meals
            List<RawDataEntry> lastMeals = DataHelper
                    .filterHistoryValues(bolusWizardList, item.timestamp, hypoMealHistoryTime);
            if (lastMeals.isEmpty()) {
                // if no value in range, show last available value
                RawDataEntry lastValue = DataHelper.filterLastValue(bolusWizardList,
                        item.timestamp);
                if (lastValue != null) {
                    sb.append("Letzte Hauptmahlzeit: ")
                            .append(lastValue.toTextListEntry()).append("\n");
                } else {
                    sb.append("Letzte Hauptmahlzeit: ")
                            .append("ERROR").append("\n");
                }
            } else {
                for (RawDataEntry meal : lastMeals) {
                    sb.append("Letzte Hauptmahlzeit: ")
                            .append(meal.toTextListEntry()).append("\n");
                }
            }
            // calc exercise
            sb.append("Stand in Zusammenhang mit körperlicher Aktivität? ");
            List<RawDataEntry> exerciseMarker = DataHelper
                    .filterHistoryValues(exerciseMarkerList, item.timestamp,
                            exerciseHistoryTime);
            if (exerciseMarker.isEmpty()) {
                sb.append("Nein.\n");
            } else {
                sb.append("Ja.\n");
            }

            sb.append("Hatten sie Anzeichen von Krankheitssymptomen? Nein.\n");

            // calc sleep
            sb.append("Haben Sie geschlafen, als die Unterzuckerung auftrat? ");
            RawDataEntry lastUserAction = DataHelper.filterLastValue(completeList,
                    item.timestamp);
            if (lastUserAction != null) {
                int lastEventMinutes = DataHelper.minutesDiff(lastUserAction.timestamp,
                        item.timestamp);
                Calendar cal = new GregorianCalendar(Locale.GERMANY);
                cal.setTime(item.timestamp);

                if (lastEventMinutes > sleepThreshold && cal.get(Calendar.HOUR) > bedTime
                        || cal.get(Calendar.HOUR_OF_DAY) < wakupTime) {
                    // inside sleep time and over threshold
                    sb.append("Ja.\nSind Sie von den Symptomen aufgewacht? Ja.\n");
                } else if (lastEventMinutes < sleepThreshold && cal.get(Calendar.HOUR) < bedTime
                        || cal.get(Calendar.HOUR_OF_DAY) > wakupTime) {
                    // there was an action and its day time
                    sb.append("Nein.\n");
                } else {
                    sb.append("NO DATA\n");
                }
            } else {
                sb.append("NO DATA\n");
            }
            sb.append("\n");
        }

        // create hyper text
        sb.append("\n\nUnerklärliche Hyperglykämien (>").append(hyperThreshold).append("):\n");
        for (RawDataEntry item : hyperList) {
            sb.append(item.toTextListEntry()).append("\n");
            // calc last meals
            List<RawDataEntry> lastMeals = DataHelper
                    .filterHistoryValues(bolusWizardList, item.timestamp, hyperMealHistoryTime);
            if (lastMeals.isEmpty()) {
                // if no value in range, show last available value
                RawDataEntry lastValue = DataHelper.filterLastValue(bolusWizardList,
                        item.timestamp);
                if (lastValue != null) {
                    sb.append("Letzte Hauptmahlzeit: ")
                            .append(lastValue.toTextListEntry()).append("\n");
                } else {
                    sb.append("Letzte Hauptmahlzeit: ")
                            .append("ERROR").append("\n");
                }
            } else {
                for (RawDataEntry meal : lastMeals) {
                    sb.append("Letzte Hauptmahlzeit: ")
                            .append(meal.toTextListEntry()).append("\n");
                }
            }
            sb.append("Keton im Blut: Nicht verfügbar.\n");
            sb.append("Keton im Urin: \n\n");
        }
        return sb.toString();
    }
}
