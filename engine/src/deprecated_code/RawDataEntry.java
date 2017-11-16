/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deprecated_code;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author juehv
 */
public class RawDataEntry {

    public String type = "";
    public Date timestamp = new Date();
    public double amount = -1.0;
    public String linkId = "";

    @Override
    public String toString() {
        return "DataEntry{" + "type=" + type + ", timestamp=" + timestamp + 
                ", amount=" + amount +'}';
    }

    public String toGuiListEntry() {
        SimpleDateFormat dformat = new SimpleDateFormat(Constants.DATE_TIME_OUTPUT_FORMAT);
        if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[1])) {
            // Prime
            return String.format("%s --> (%s)", dformat.format(timestamp), type);
        } else if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[2])) {
            // exercise marker
            return String.format("%s --> %.1f (%s)", dformat.format(timestamp),
                    amount, type);
        } else if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[4])) {
            // BG
            return String.format("%s --> %.1f (%s)", dformat.format(timestamp),
                    amount, type);
        } else if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[5])) {
            // Bolus Wizard (interested in KEs)
            return String.format("%s --> %.1f BE (%s)", dformat.format(timestamp),
                    amount, type);
        }
        if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[6])) {
            // Bolus given
            return String.format("%s --> %.2f I.E. (%s)", dformat.format(timestamp),
                    amount, type);
        }
        return toString();
    }

    public String toTextListEntry() {
        SimpleDateFormat dformat = new SimpleDateFormat(Constants.DATE_TIME_OUTPUT_FORMAT);
        if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])) {
            // rewind
            return String.format("%s", dformat.format(timestamp));
        } else if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[1])) {
            // Prime
            return String.format("%s (%s)", dformat.format(timestamp), type);
        } else if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[2])) {
            // exercise marker
            return "ERROR";
        } else if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[4])) {
            // BG
            return String.format("%s %.1f", dformat.format(timestamp),
                    amount);
        } else if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[5])) {
            // Bolus Wizard (interested in KEs)
            return String.format("%s (%.1f)", dformat.format(timestamp),
                    amount);
        }
        if (type.equalsIgnoreCase(Constants.CARELINK_TYPE[6])) {
            // Bolus given
            return "ERROR";
        }
        return toString();
    }

    public boolean isEquivalent(RawDataEntry item) {
        if (item == null) {
            return false;
        }
        if (this == item) {
            return true;
        }

        if ((type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[1]))
                && ((item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[0])
                || item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[1])))) {
            // primes
            if (DataHelper.minutesDiff(this.timestamp, item.timestamp) > 15) {
                return false;
            }
        } else if ((type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[4]))
                && ((item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[3])
                || item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[4])))) {
            //bg
            if (DataHelper.minutesDiff(this.timestamp, item.timestamp) > 5) {
                return false;
            }
            if (this.amount - item.amount > 0.0001) {
                return false;
            }
        } else if ((type.equalsIgnoreCase(Constants.CARELINK_TYPE[5])
                || type.equalsIgnoreCase(Constants.CARELINK_TYPE[6]))
                && ((item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[5])
                || item.type.equalsIgnoreCase(Constants.CARELINK_TYPE[6])))) {
            // bolus 
            if (DataHelper.minutesDiff(this.timestamp, item.timestamp) > 1) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public static Comparator<RawDataEntry> getTimeSortComparator() {

        Comparator<RawDataEntry> comp = new Comparator<RawDataEntry>() {

            @Override
            public int compare(RawDataEntry a, RawDataEntry b) {
                return a.timestamp.compareTo(b.timestamp);
            }
        };

        return comp;
    }

}
