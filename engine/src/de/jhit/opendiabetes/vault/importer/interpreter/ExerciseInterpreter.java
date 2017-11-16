/*
 * Copyright (C) 2017 Jens Heuschkel
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
package de.jhit.opendiabetes.vault.importer.interpreter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryAnnotation;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.data.VaultDao;
import de.jhit.opendiabetes.vault.importer.Importer;
import de.jhit.opendiabetes.vault.util.EasyFormatter;
import de.jhit.opendiabetes.vault.util.SortVaultEntryByDate;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Jens
 */
public class ExerciseInterpreter extends VaultInterpreter {

    private final ExerciseInterpreterOptions myOptions;

    public ExerciseInterpreter(Importer importer,
            ExerciseInterpreterOptions options, VaultDao db) {
        super(importer, options, db);
        myOptions = options;
    }

    @Override
    protected List<VaultEntry> interpret(List<VaultEntry> result) {
        List<VaultEntry> data = result;

        // sort by date
        Collections.sort(data, new SortVaultEntryByDate());

        LOG.finer("Start activity data interpretation");
        data = filterActititys(data);
        data = interpretStress(data);

        LOG.finer("Tracker data interpretation finished");

        return data;
    }

    private List<VaultEntry> filterActititys(List<VaultEntry> data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        List<VaultEntry> retVal = new ArrayList<>();
        List<VaultEntry> dbValues = db.queryExerciseBetween(
                data.get(0).getTimestamp(),
                data.get(data.size() - 1).getTimestamp());
        VaultEntry lastExerciseItem = null;

        List<VaultEntryAnnotation> annotations = new ArrayList<>();

        for (VaultEntry item : data) {
            switch (item.getType()) {
                case EXERCISE_BICYCLE:
                case EXERCISE_RUN:
                case EXERCISE_WALK:
                case EXERCISE_OTHER:
                    // google activity type > tracker activity type
                    if (lastExerciseItem == null) {
                        // init item
                        lastExerciseItem = item;
                        // init annotations
                        annotations.addAll(item.getAnnotations());
                    } else if (Math.round(
                            (item.getTimestamp().getTime()
                            - lastExerciseItem.getTimestamp().getTime())
                            / 60000)
                            >= Math.round(myOptions.activitySliceThreshold
                                    + lastExerciseItem.getValue())) {
                        // within a slice
                        lastExerciseItem.setValue(item.getValue() + lastExerciseItem.getValue()); // add time to value

                        // update annotations
                        annotations = mergeAnnotations(annotations, item.getAnnotations());
                    } else {
                        // new slice --> precess current slice and reset

                        // check db for existing entries
                        for (VaultEntry historyEntry : dbValues) {
                            if (isHistoricElementWithinSlice(lastExerciseItem, historyEntry)) { // is within duration
                                // merge entries
                                //db.removeEntry(historyEntry); // TODO move this function in parent class, return hashmap with matchin historic elements

                                // check if db activity is longer
                                if (TimestampUtils.addMinutesToTimestamp(
                                        historyEntry.getTimestamp(),
                                        Math.round(historyEntry.getValue())).after(
                                        TimestampUtils.addMinutesToTimestamp(
                                                lastExerciseItem.getTimestamp(),
                                                Math.round(lastExerciseItem.getValue())))) {
                                    // extend exercise time
                                    long durationInMilli = TimestampUtils.addMinutesToTimestamp(
                                            historyEntry.getTimestamp().getTime(),
                                            Math.round(historyEntry.getValue()))
                                            - TimestampUtils.addMinutesToTimestamp(
                                                    lastExerciseItem.getTimestamp().getTime(),
                                                    Math.round(lastExerciseItem.getValue()));
                                    lastExerciseItem.setValue(lastExerciseItem.getValue()
                                            + Math.round((durationInMilli / 60000)));
                                }

                                annotations = mergeAnnotations(annotations, historyEntry.getAnnotations());
                            } else if (lastExerciseItem != null
                                    && historyEntry.getTimestamp().after(lastExerciseItem.getTimestamp())
                                    && Math.round(historyEntry.getTimestamp().getTime() / 60000)
                                    > Math.round(lastExerciseItem.getValue())) {
                                // we passed the current time point --> stop searching
                                break;
                            }
                        }
                        // save old slice
                        if (lastExerciseItem != null
                                && lastExerciseItem.getValue() > myOptions.activityThreshold) {
                            // define type
                            double walk = 0.0;
                            double run = 0.0;
                            double bike = 0.0;
                            double other = 0.0;
                            for (VaultEntryAnnotation annotation : annotations) {
                                if (!annotation.getValue().isEmpty()) {
                                    try {
                                        switch (annotation.getType()) {
                                            case EXERCISE_GoogleBicycle:
                                            case EXERCISE_TrackerBicycle:
                                                bike += Double.parseDouble(annotation.getValue());
                                                break;
                                            case EXERCISE_GoogleRun:
                                            case EXERCISE_TrackerRun:
                                                run += Double.parseDouble(annotation.getValue());
                                                break;
                                            case EXERCISE_GoogleWalk:
                                            case EXERCISE_TrackerWalk:
                                                walk += Double.parseDouble(annotation.getValue());
                                                break;
                                            case EXERCISE_AUTOMATIC_OTHER:
                                                other += Double.parseDouble(annotation.getValue());
                                                break;
                                            default:
                                                LOG.severe("ASSERTION ERROR in exercice type estimation");
                                                throw new AssertionError("Case not allowed here !!");
                                        }
                                    } catch (NumberFormatException ex) {
                                        LOG.log(Level.SEVERE, "Error parsing generated double while exercise type estimation", ex);
                                    }
                                }
                            }
                            // get type with biggest share
                            if (bike > run && bike > walk && bike > other) {
                                lastExerciseItem.setType(VaultEntryType.EXERCISE_BICYCLE);
                            } else if (run > bike && run > walk && run > other) {
                                lastExerciseItem.setType(VaultEntryType.EXERCISE_RUN);
                            } else if (walk > bike && walk > run && walk > other) {
                                lastExerciseItem.setType(VaultEntryType.EXERCISE_WALK);
                            } else {
                                lastExerciseItem.setType(VaultEntryType.EXERCISE_OTHER);
                            }
                            retVal.add(lastExerciseItem);
                        }

                        //  setup for new slice search
                        lastExerciseItem = item;
                        annotations = new ArrayList<>();
                        annotations.addAll(item.getAnnotations());

                    }
                    break;
                case EXERCISE_MANUAL:
                    // add manula entrys without interpretation
                    retVal.add(item);
                    break;
                default:
                    retVal.add(item);
            }
        }

        // add last unsliced item
        if (lastExerciseItem != null
                && lastExerciseItem.getValue() > myOptions.activityThreshold) {
            retVal.add(lastExerciseItem);
        }

        return retVal;
    }

    private List<VaultEntryAnnotation> mergeAnnotations(List<VaultEntryAnnotation> currentAnnotations,
            List<VaultEntryAnnotation> additionalAnnotations) {
        if (currentAnnotations == null || additionalAnnotations == null) {
            return currentAnnotations;
        }
        for (VaultEntryAnnotation itemAnnoation : additionalAnnotations) {
            boolean merged = false;
            for (VaultEntryAnnotation seenAnnotaion : currentAnnotations) {
                if (itemAnnoation.toString().equalsIgnoreCase(seenAnnotaion.toString())) {
                    try {
                        if (!itemAnnoation.getValue().isEmpty()
                                && !seenAnnotaion.getValue().isEmpty()) { // merge two values
                            seenAnnotaion.setValue(EasyFormatter.formatDouble(
                                    Double.parseDouble(seenAnnotaion.getValue())
                                    + Double.parseDouble(itemAnnoation.getValue())));
                        } else if (!itemAnnoation.getValue().isEmpty()
                                && seenAnnotaion.getValue().isEmpty()) { // add value
                            seenAnnotaion.setValue(itemAnnoation.getValue());
                        }

                    } catch (NumberFormatException ex) {
                        LOG.log(Level.SEVERE, "Error parsing generated double while exercise annotation merging", ex);
                    }
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                currentAnnotations.add(itemAnnoation);
            }
        }
        return currentAnnotations;
    }

    private boolean isHistoricElementWithinSlice(VaultEntry item, VaultEntry historyElement) {
        return item != null && historyElement != null // not null
                && TimestampUtils.addMinutesToTimestamp(historyElement.getTimestamp(), -1 * myOptions.activitySliceThreshold).after(item.getTimestamp()) // starts after current item (with respect to slice threshold)
                && TimestampUtils.addMinutesToTimestamp(item.getTimestamp(), Math.round(item.getValue() + myOptions.activitySliceThreshold)).after(historyElement.getTimestamp()); // starts befor item ends (with respect to slice threshold)
    }

    private List<VaultEntry> interpretStress(List<VaultEntry> data) {
        List<VaultEntry> addData = new ArrayList<>();
        VaultEntry lastItem = null;
        for (VaultEntry item : data) {
            if (item.getType() == VaultEntryType.STRESS) {
                if (lastItem == null) {
                    //warm up 
                    lastItem = item;
                } else {
                    if (item.getTimestamp().getTime()
                            - lastItem.getTimestamp().getTime() > 6300000) { // > 10.5 min
                        // add entry with 0, 10 minutes after last timestamp
                        addData.add(new VaultEntry(VaultEntryType.STRESS,
                                TimestampUtils.addMinutesToTimestamp(
                                        lastItem.getTimestamp(), 10), 0));
                    }

                    lastItem = item;
                }

            }
        }

        // check last item again
        if (lastItem != null
                && data.get(data.size() - 1).getTimestamp().getTime()
                - lastItem.getTimestamp().getTime() > 6300000) { // > 10.5 min
            // add entry with 0 
            addData.add(new VaultEntry(VaultEntryType.STRESS,
                    TimestampUtils.addMinutesToTimestamp(
                            lastItem.getTimestamp(), 10), 0));
        }

        data.addAll(addData);
        Collections.sort(data, new SortVaultEntryByDate());

        return data;
    }
}
