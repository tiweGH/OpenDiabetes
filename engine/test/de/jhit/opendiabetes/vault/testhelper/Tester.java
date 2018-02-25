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
import de.jhit.opendiabetes.vault.container.FinalBucketEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.jhit.opendiabetes.vault.processing.BucketProcessor;

import de.jhit.opendiabetes.vault.exporter.MLExporter1;
import de.jhit.opendiabetes.vault.processing.BucketProcessor;
import de.jhit.opendiabetes.vault.processing.BucketProcessor_new;
import de.jhit.opendiabetes.vault.processing.BucketProcessor_runable;
import de.jhit.opendiabetes.vault.processing.DataSlicer;
import de.jhit.opendiabetes.vault.processing.DataSlicerOptions;
import de.jhit.opendiabetes.vault.processing.VaultEntrySlicer;
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
import de.jhit.opendiabetes.vault.testhelper.filterfactory.GlucoseElevation;
import de.jhit.opendiabetes.vault.testhelper.filterfactory.ExcludeTimespanWithCriteria;
import de.jhit.opendiabetes.vault.testhelper.filterfactory.NigthAutoSuspendBolus4h;
import de.jhit.opendiabetes.vault.testhelper.filterfactory.NoDateTimeSpansWithoutGroup;
import de.jhit.opendiabetes.vault.testhelper.filterfactory.TypeAbsence;
import de.jhit.opendiabetes.vault.testhelper.filterfactory.TypeAfterNthEventAfterEvent;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import de.jhit.opendiabetes.vault.util.VaultEntryUtils;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
            List<VaultEntry> sensyList = SensitivityDataset.getSensitivityDataset();
            List<VaultEntry> custSet = CustomDataset.getCustomDataset();
            List<VaultEntry> workingSet = entryList;
            List<Filter> fl = new ArrayList<>();
            FilterResult res;

//            Filter fil
//                    = new NegateFilter(
//                            new QueryFilter(new TimeSpanFilter(LocalTime.of(6, 0), LocalTime.of(8, 0)), new EventFilter(VaultEntryType.GLUCOSE_CGM), 5, -1));
//            VaultEntrySlicer slic = new VaultEntrySlicer();
//            FilterFactory fac = new ExcludeTimespanWithCriteria(LocalTime.of(11, 0), 120);
//
//            slic.registerFilter(fac.createFilter());//new TypeGroupFilter(VaultEntryTypeGroup.GLUCOSE) );
//            //   slic.addClustering(2 * 60, VaultEntryType.GLUCOSE_CGM, VaultEntryType.CLUSTER_GLUCOSE_CGM);
//
//            res = slic.sliceEntries(workingSet);
//            fl.add(new PositionFilter(PositionFilter.DATE_MIDDLE));
//            fil = new TimeClusterFilter(fl, 120, 60);
//            //res = fil.filter(workingSet);
//            for (VaultEntry vaultEntry : res.filteredData) {
//                System.out.println(vaultEntry.toString());
//            }
//            System.out.println(workingSet.size());
//            System.out.println(res.size() + " " + res.timeSeries.size());
//            long temp1, temp2;
            //VaultEntry tmp = res.filteredData.get(res.filteredData.size() - 1);
//            System.out.println("Missing: ");
//            for (VaultEntry vaultEntry : workingSet) {
//                if (!res.filteredData.contains(vaultEntry)) {
//                    System.out.println(vaultEntry.getTimestamp());
//                }
//            }
//            Date tmp = VaultEntryUtils.getWeightedMiddle(workingSet, VaultEntryType.GLUCOSE_CGM);
//            System.out.println(TimestampUtils.getMidDate(workingSet.get(0).getTimestamp(), workingSet.get(workingSet.size() - 1).getTimestamp()));
//            System.out.println(tmp);
            MLExporter1 mle = new MLExporter1();
            BucketProcessor_new bp = new BucketProcessor_new();
//            BucketProcessor bp = new BucketProcessor();
//            BucketProcessor_runable bp = new BucketProcessor_runable();
              List<FinalBucketEntry> buckets = bp.bucketProcessing(0, workingSet, 1);
    //        List<FinalBucketEntry> buckets = bp.processor(workingSet, 1);
            MLExporter1.bucketsToCsv(buckets, "odv_export.csv");
            long minutes = (workingSet.get(workingSet.size() - 1).getTimestamp().getTime() - workingSet.get(0).getTimestamp().getTime()) / (60 * 1000);
            System.out.println("Test_end_output_minutes_:");
            System.out.println(minutes);

        } catch (ParseException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
