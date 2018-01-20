package de.jhit.opendiabetes.vault.testhelper;

/*
 * Copyright (C) 2017 tiweGH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.jhit.opendiabetes.vault.processing.filter.CombinationFilter;
import de.jhit.opendiabetes.vault.processing.filter.ContinuousWrapper;
import de.jhit.opendiabetes.vault.processing.filter.EventFilter;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.FilterResult;
import de.jhit.opendiabetes.vault.processing.filter.*;
import de.jhit.opendiabetes.vault.processing.filter.MealAbsenceFilter;
import de.jhit.opendiabetes.vault.processing.filter.NoneFilter;
import de.jhit.opendiabetes.vault.processing.filter.OverThresholdFilter;
import de.jhit.opendiabetes.vault.processing.filter.TimePointFilter;
import de.jhit.opendiabetes.vault.processing.filter.TypeAbsenceFilter;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author tiweGH
 */
public class Tester {

    public static void main(String[] args) {
        try {
//            NoneFilter nf = new NoneFilter();
//            System.out.println(StaticDataset.getStaticDataset().size());
//            System.out.println(TimestampUtils.getNormalizedTimeSeries(StaticDataset.getStaticDataset(), 0).size());
//            System.out.println(nf.filter(StaticDataset.getStaticDataset()).timeSeries.size() + " " + nf.filter(StaticDataset.getStaticDataset()).filteredData.size());
//            List<VaultEntry> entries = StaticDataset.getStaticDataset();
//            List<Pair<Date, Date>> result = TimestampUtils.getNormalizedTimeSeries(StaticDataset.getStaticDataset(), 0);
//            for (VaultEntry entry : entries) {
//                boolean isso = false;
//                for (Pair<Date, Date> pair : result) {
//                    isso = TimestampUtils.withinDateTimeSpan(pair.getKey(), pair.getValue(), entry.getTimestamp()) || isso;
//                    if (TimestampUtils.withinDateTimeSpan(pair.getKey(), pair.getValue(), entry.getTimestamp())) {
//                        if (!pair.getKey().equals(pair.getValue())) {
//                            System.out.println(entry.getTimestamp().toString() + " is in " + pair.toString());
//                        } else {
//                            System.out.println(pair.toString());
//                        }
//                    }
//                }
//                if (!isso) {
//                    System.out.println(entry.getTimestamp().toString() + " has no result");
//                }
//            }

            List<VaultEntry> entryList = StaticDataset.getStaticDataset();
            FilterResult fr;
            DatasetMarker f2 = new DatasetMarker(entryList);
            fr = f2.filter(entryList);
            fr = new DateTimeSpanFilter(TimestampUtils.createCleanTimestamp("2017.06.29-06:00", "yyyy.MM.dd-HH:mm"), TimestampUtils.createCleanTimestamp("2017.06.29-08:00", "yyyy.MM.dd-HH:mm")).filter(entryList);
            Filter f1 = new CombinationFilter1(f2, new TypeGroupFilter(VaultEntryTypeGroup.BASAL), new TimePointFilter(LocalTime.MIN, 0, 10));
            //f1 = new NegateFilter(new TimePointFilter(TimestampUtils.dateToLocalTime(entryList.get(0).getTimestamp()), 20));
            fr = f2.filter(fr.filteredData);
            fr = f1.filter(fr.filteredData);
            for (VaultEntry entry : fr.filteredData) {
                System.out.println(entry.getTimestamp().toString());
            }
            System.out.println(f2.getDataset().size());
            System.out.println(fr.filteredData.size());

        } catch (ParseException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
