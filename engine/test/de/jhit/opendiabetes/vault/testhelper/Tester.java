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
import de.jhit.opendiabetes.vault.testhelper.filterfactory.BolusCGMLess60;
import de.jhit.opendiabetes.vault.testhelper.filterfactory.BolusGreater180NoMeal3h;
import de.jhit.opendiabetes.vault.testhelper.filterfactory.FilterFactory;
import de.jhit.opendiabetes.vault.testhelper.filterfactory.NigthAutoSuspendBolus4h;
import de.jhit.opendiabetes.vault.testhelper.filterfactory.TypeAbsence;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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

            List<VaultEntry> entryList = StaticDataset.getStaticDataset();
            FilterResult fr;
            FilterFactory fac = new TypeAbsence(entryList, VaultEntryTypeGroup.GLUCOSE, 10);
            List<Filter> fl = fac.createFilter();
            FilterResult lastResult = null;

            for (Filter filter : fl) {
                if (lastResult == null) {
                    lastResult = filter.filter(entryList);
                } else {
                    lastResult = filter.filter(lastResult.filteredData);
                }
            }

            for (VaultEntry entry : lastResult.filteredData) {
                //System.out.println(entry.toString());
            }
            LocalDate localStart = TimestampUtils.dateToLocalDate(TimestampUtils.createCleanTimestamp("2017.06.20-04:53", "yyyy.MM.dd-HH:mm"));
            LocalDate localEnd = TimestampUtils.dateToLocalDate(TimestampUtils.createCleanTimestamp("2017.06.30-04:53", "yyyy.MM.dd-HH:mm"));
            if (localEnd.isAfter(localStart)) {
                int yearOffset = 0;
                //   LocalDate tempDate = localEnd.minus(localStart.toEpochDay(),);
                int dayOffset = localEnd.minusDays(localStart.getDayOfYear()).getDayOfYear();
                System.out.println(dayOffset + " " + yearOffset);
                yearOffset = localEnd.minusYears(localStart.getYear()).getYear();
                System.out.println(dayOffset + " " + yearOffset);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
