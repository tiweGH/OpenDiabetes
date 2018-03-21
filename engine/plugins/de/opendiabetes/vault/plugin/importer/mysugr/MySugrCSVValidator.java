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
package de.opendiabetes.vault.plugin.importer.mysugr;

import com.csvreader.CsvReader;
import de.opendiabetes.vault.container.VaultEntryAnnotation;
import de.opendiabetes.vault.plugin.importer.validator.CSVValidator;
import de.opendiabetes.vault.plugin.util.TimestampUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * This class is used to validate MySugr based data and to extract the values from the file.
 *
 * @author Jens Heuschkel
 * @author Lucas Buschlinger
 */
public class MySugrCSVValidator extends CSVValidator {

    /**
     * Pattern to indicate German date header.
     */
    private static final String MY_SUGR_HEADER_DE_DATE = "Datum";
    /**
     * Pattern to indicate German time header.
     */
    private static final String MY_SUGR_HEADER_DE_TIME = "Zeit";
    /**
     * Pattern to indicate German insulin injection meal units header.
     */
    private static final String MY_SUGR_HEADER_DE_INSULIN_INJECTION_UNITS_MEAL = "Bolus (Mahlzeit)";
    /**
     * Pattern to indicate German insulin injection correction units header.
     */
    private static final String MY_SUGR_HEADER_DE_INSULIN_INJECTION_UNITS_CORRECTION = "Bolus (Korrektur)";
    /**
     * Pattern to indicate German blood glucose measurement header.
     */
    private static final String MY_SUGR_HEADER_DE_BLOOD_GLUCOSE_MEASUREMENT = "Blutzuckermessung (mg/dL)";
    /**
     * Pattern to indicate German meal manual header.
     */
    private static final String MY_SUGR_HEADER_DE_MEAL_MANUAL = "Mahlzeitkohlenhydrate (Broteinheiten, Faktor 12)";
    /**
     * Pattern to indicate German basal injection units header.
     */
    private static final String MY_SUGR_HEADER_DE_BASAL_INJECTION_UNITS = "Basalinjektionseinheiten";
    /**
     * Pattern to indicate German bolus header.
     */
    private static final String MY_SUGR_HEADER_DE_BOLUS = "Bolus Normal"; //self created one, for saving entries in Vault
    /**
     * Pattern to indicate German exercise header.
     */
    private static final String MY_SUGR_HEADER_DE_EXERCISE = "Aktivitätsdauer (Minuten)";
    /**
     * Pattern to indicate German exercise intensity header.
     */
    private static final String MY_SUGR_HEADER_DE_EXERCISE_INTENSITY = "Aktivitätsintensität (1: Bequem, 2: Normal, 3: Anstrengend)";
    /**
     * Pattern to indicate German ketones header.
     */
    private static final String MY_SUGR_HEADER_DE_KETONES = "Ketone";
    /**
     * Pattern to indicate German insulin injection pen units header.
     */
    private static final String MY_SUGR_HEADER_DE_INSULIN_INJECTION_UNITS_PEN = "Bolusinjektionseinheiten (Pen)";
    /**
     * Pattern to indicate German insulin injection pump units header.
     */
    private static final String MY_SUGR_HEADER_DE_INSULIN_INJECTION_UNITS_PUMP = "Bolusinjektionseinheiten (Pump)";
    /**
     * Pattern to indicate German blood pressure header.
     */
    private static final String MY_SUGR_HEADER_DE_BLOOD_PRESSURE = "Blutdruck";
    /**
     * Pattern to indicate German meal descriptions header.
     */
    private static final String MY_SUGR_HEADER_DE_MEAL_DESCRIPTIONS = "Mahlzeitbeschreibung";
    /**
     * Pattern to indicate German food type header.
     */
    private static final String MY_SUGR_HEADER_DE_FOOD_TYPE = "Art der Nahrung";
    /**
     * Pattern to indicate German tags header.
     */
    private static final String MY_SUGR_HEADER_DE_TAGS = "Tags";
    /**
     * Pattern to indicate German note header.
     */
    private static final String MY_SUGR_HEADER_DE_NOTE = "Notiz";
    /**
     * German date time format.
     */
    private static final String TIME_FORMAT_DE = "MM.dd.yy HH:mm:ss";


    /**
     * Pattern to indicate English date header.
     */
    private static final String MY_SUGR_HEADER_EN_DATE = "Date";
    /**
     * Pattern to indicate English time header.
     */
    private static final String MY_SUGR_HEADER_EN_TIME = "Time";
    /**
     * Pattern to indicate English insulin injection meal units header.
     */
    private static final String MY_SUGR_HEADER_EN_INSULIN_INJECTION_UNITS_MEAL = "Insulin (Meal)";
    /**
     * Pattern to indicate English insulin injection correction units header.
     */
    private static final String MY_SUGR_HEADER_EN_INSULIN_INJECTION_UNITS_CORRECTION = "Insulin (Correction)";
    /**
     * Pattern to indicate English blood glucose measurement header.
     */
    private static final String MY_SUGR_HEADER_EN_BLOOD_GLUCOSE_MEASUREMENT = "Blood Glucose Measurement (mg/dL)";
    /**
     * Pattern to indicate English meal manual header.
     */
    private static final String MY_SUGR_HEADER_EN_MEAL_MANUAL = "Meal Carbohydrates (Grams, Factor 1)";
    /**
     * Pattern to indicate English basal injection units header.
     */
    private static final String MY_SUGR_HEADER_EN_BASAL_INJECTION_UNITS = "Basal Injection Units";
    /**
     * Pattern to indicate English bolus header.
     */
    private static final String MY_SUGR_HEADER_EN_BOLUS = "Bolus Normal"; //self created one, for saving entries in Vault
    /**
     * Pattern to indicate English exercises header.
     */
    private static final String MY_SUGR_HEADER_EN_EXERCISE = "Activity Duration (Minutes)";
    /**
     * Pattern to indicate English exercise intensity header.
     */
    private static final String MY_SUGR_HEADER_EN_EXERCISE_INTENSITY = "Activity Intensity (1: Cosy, 2: Ordinary, 3: Demanding)";
    /**
     * Pattern to indicate English ketones header.
     */
    private static final String MY_SUGR_HEADER_EN_KETONES = "Ketones";
    /**
     * Pattern to indicate English insulin injection pen units header.
     */
    private static final String MY_SUGR_HEADER_EN_INSULIN_INJECTION_UNITS_PEN = "Insulin Injection Units (Pen)";
    /**
     * Pattern to indicate English insulin injection pump units header.
     */
    private static final String MY_SUGR_HEADER_EN_INSULIN_INJECTION_UNITS_PUMP = "Insulin Injection Units (pump)";
    /**
     * Pattern to indicate English blood pressure.
     */
    private static final String MY_SUGR_HEADER_EN_BLOOD_PRESSURE = "Blood pressure";
    /**
     * Pattern to indicate English meal descriptions header.
     */
    private static final String MY_SUGR_HEADER_EN_MEAL_DESCRIPTIONS = "Meal Descriptions";
    /**
     * Pattern to indicate English food type header.
     */
    private static final String MY_SUGR_HEADER_EN_FOOD_TYPE = "Food type";
    /**
     * Pattern to indicate English tags header.
     */
    private static final String MY_SUGR_HEADER_EN_TAGS = "Tags";
    /**
     * Pattern to indicate English note header.
     */
    private static final String MY_SUGR_HEADER_EN_NOTE = "Note";
    /**
     * English date time format.
     */
    private static final String TIME_FORMAT_EN = "MM.dd.yy HH:mm:ss";

    /**
     * The length of the parsed date.
     */
    private static final int DATE_PARTS_LENGTH = 3;

    /**
     * The max hour of the time format.
     */
    private static final int TIME_MAX_HOUR = 12;

    /**
     * German header entries.
     */
    private static final String[] HEADER_DE = {
            MY_SUGR_HEADER_DE_DATE, MY_SUGR_HEADER_DE_TIME,
            MY_SUGR_HEADER_DE_BLOOD_GLUCOSE_MEASUREMENT,
            MY_SUGR_HEADER_DE_BASAL_INJECTION_UNITS,
            MY_SUGR_HEADER_DE_INSULIN_INJECTION_UNITS_MEAL,
            MY_SUGR_HEADER_DE_MEAL_MANUAL,

    };

    /**
     * English header entries.
     */
    private static final String[] HEADER_EN = {
            MY_SUGR_HEADER_EN_DATE, MY_SUGR_HEADER_EN_TIME, MY_SUGR_HEADER_EN_INSULIN_INJECTION_UNITS_MEAL,
            MY_SUGR_HEADER_EN_BLOOD_GLUCOSE_MEASUREMENT, MY_SUGR_HEADER_EN_MEAL_MANUAL,
            MY_SUGR_HEADER_EN_BASAL_INJECTION_UNITS,
    };

    /**
     * Constructor for MySugrCSVValidator.
     */
    public MySugrCSVValidator() {
        super(HEADER_DE, HEADER_EN);
    }

    /**
     * Returns the insulin meal for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The insulin meal.
     * @throws IOException If the file could not be opened.
     */
    public String getInsulinMeal(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_INSULIN_INJECTION_UNITS_MEAL);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_INSULIN_INJECTION_UNITS_MEAL);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the insulin injection correction units for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The insulin injection correction units.
     * @throws IOException If the file could not be opened.
     */
    public String getInsulinCorrection(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_INSULIN_INJECTION_UNITS_CORRECTION);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_INSULIN_INJECTION_UNITS_CORRECTION);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the insulin pen injection units for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The insulin injection pen units.
     * @throws IOException If the file could not be opened.
     */
    private String getInsulinPen(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_INSULIN_INJECTION_UNITS_PEN);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_INSULIN_INJECTION_UNITS_PEN);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the insulin pump injection units for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The insulin injection pump units.
     * @throws IOException If the file could not be opened.
     */
    private String getInsulinPump(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_INSULIN_INJECTION_UNITS_PUMP);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_INSULIN_INJECTION_UNITS_PUMP);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the blood glucose measurement for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The blood glucose measurement.
     * @throws IOException If the file could not be opened.
     */
    public String getBGMeasurement(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_BLOOD_GLUCOSE_MEASUREMENT);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_BLOOD_GLUCOSE_MEASUREMENT);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the meal carbs for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The blood glucose measurement.
     * @throws IOException If the file could not be opened.
     */
    public String getMealCarbs(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_MEAL_MANUAL);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_MEAL_MANUAL);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the basal injection units for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The basal injection units.
     * @throws IOException If the file could not be opened.
     */
    public String getBasalUnits(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_BASAL_INJECTION_UNITS);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_BASAL_INJECTION_UNITS);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the manual timestamp for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The manual timestamp.
     * @throws IOException If the file could not be opened.
     * @throws ParseException If there was an error while parsing.
     */
    public Date getManualTimestamp(final CsvReader creader) throws IOException, ParseException {
        String dateString;
        String timeString;
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                dateString = creader.get(MY_SUGR_HEADER_DE_DATE).trim();
                timeString = creader.get(MY_SUGR_HEADER_DE_TIME).trim();
                return TimestampUtils.createCleanTimestamp(
                        dateString + " " + timeString, TIME_FORMAT_DE);
            case EN:
                dateString = dateFormatter(creader.get(MY_SUGR_HEADER_EN_DATE).trim());
                timeString = timeFormatter(creader.get(MY_SUGR_HEADER_EN_TIME).trim());
                return TimestampUtils.createCleanTimestamp(
                        dateString + " " + timeString, TIME_FORMAT_EN);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the normal bolus for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The normal bolus.
     * @throws IOException If the file could not be opened.
     */
    public String getBolusNormal(final CsvReader creader) throws IOException {
        String tempMeal = getInsulinMeal(creader);
        String tempCorrection = getInsulinCorrection(creader);
        String tempPen = getInsulinPen(creader);
        String tempPump = getInsulinPump(creader);
        double insulinMeal = 0, insulinCorrection = 0, insulinPen = 0, insulinPump = 0;
        if (!tempMeal.isEmpty()) {
            insulinMeal = Double.parseDouble(tempMeal.replace(",", "."));
        }
        if (!tempCorrection.isEmpty()) {
            insulinCorrection = Double.parseDouble(tempCorrection.replace(",", "."));
        }
        if (!tempPen.isEmpty()) {
            insulinPen = Double.parseDouble(tempPen.replace(",", "."));
        }
        if (!tempPump.isEmpty()) {
            insulinPump = Double.parseDouble(tempPump.replace(",", "."));
        }
        if (insulinMeal + insulinCorrection + insulinPen + insulinPump != 0) {
            return Double.toString(insulinMeal + insulinCorrection + insulinPen + insulinPump);
        }

        return "";
    }

    /**
     * Returns the activity for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The activity.
     * @throws IOException If the file could not be opened.
     */
    public String getActivity(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_EXERCISE);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_EXERCISE);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the ketones for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The ketones.
     * @throws IOException If the file could not be opened.
     */
    public String getKetones(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_KETONES);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_KETONES);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the blood pressure for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The blood pressure.
     * @throws IOException If the file could not be opened.
     */
    public String getBloodPressure(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_BLOOD_PRESSURE);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_BLOOD_PRESSURE);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the activity intensity for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The activity intensity.
     * @throws IOException If the file could not be opened.
     */
    public String getActivityIntensity(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_EXERCISE_INTENSITY);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_EXERCISE_INTENSITY);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the meal descriptions for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The meal descriptions.
     * @throws IOException If the file could not be opened.
     */
    public String getMealDescriptions(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_MEAL_DESCRIPTIONS);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_MEAL_DESCRIPTIONS);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the food type for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The food type.
     * @throws IOException If the file could not be opened.
     */
    public String getFoodType(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_FOOD_TYPE);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_FOOD_TYPE);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the tags for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The tags.
     * @throws IOException If the file could not be opened.
     */
    public String getTags(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_TAGS);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_TAGS);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the notes for the given CsvReader.
     *
     * @param creader The CsvReader instance.
     * @return String The notes.
     * @throws IOException If the file could not be opened.
     */
    public String getNotes(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return creader.get(MY_SUGR_HEADER_DE_NOTE);
            case EN:
                return creader.get(MY_SUGR_HEADER_EN_NOTE);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Returns the tag annotation for the given tag.
     *
     * @param tag the query tag.
     * @return the annotation type that was found.
     */
    public VaultEntryAnnotation.TYPE getTagAnnotation(final String tag) {
        switch (tag.trim()) { // Add more tags and OR with german text for tags.
            case "Before the meal":
                return VaultEntryAnnotation.TYPE.TAG_BeforeTheMeal;
            case "Breakfast":
                return VaultEntryAnnotation.TYPE.TAG_Breakfast;
            case "Correction":
                return VaultEntryAnnotation.TYPE.TAG_Correction;
            case "Sports":
                return VaultEntryAnnotation.TYPE.TAG_Sports;
            case "Snack":
                return VaultEntryAnnotation.TYPE.TAG_Snack;
            case "Dinner":
                return VaultEntryAnnotation.TYPE.TAG_Dinner;
            case "Office work":
                return VaultEntryAnnotation.TYPE.TAG_OfficeWork;
            case "Lunch":
                return VaultEntryAnnotation.TYPE.TAG_Lunch;
            case "Bedtime":
                return VaultEntryAnnotation.TYPE.TAG_Bedtime;
            case "Hypo feeling":
                return VaultEntryAnnotation.TYPE.TAG_HypoFeeling;
            case "Sad":
                return VaultEntryAnnotation.TYPE.TAG_Sad;
            case "Sick":
                return VaultEntryAnnotation.TYPE.TAG_Sick;
            case "After the meal":
                return VaultEntryAnnotation.TYPE.TAG_AfterTheMeal;
            case "Hyper feeling":
                return VaultEntryAnnotation.TYPE.TAG_HyperFeeling;
            case "Party":
                return VaultEntryAnnotation.TYPE.TAG_Party;
            case "Bingeing":
                return VaultEntryAnnotation.TYPE.TAG_Bingeing;
            case "Alcohol":
                return VaultEntryAnnotation.TYPE.TAG_Alcohol;
            case "Nervous":
                return VaultEntryAnnotation.TYPE.TAG_Nervous;
            case "Stress":
                return VaultEntryAnnotation.TYPE.TAG_Stress;
            default:
                return VaultEntryAnnotation.TYPE.TAG_Unknown; // returns unhandled tags with values.
        }
    }

    /**
     * Formats the given dateString to a unified format.
     *
     * @param dateString The input date string.
     * @return The formatted date String.
     */
    private String dateFormatter(final String dateString) {
        String[] dateArray = dateString.split(" ");
        if (dateArray.length != DATE_PARTS_LENGTH) {
            dateArray = dateString.split("/");
            return dateArray[1] + "." + dateArray[0] + "." + dateArray[2].substring(2);
        }
        switch (dateArray[0]) { //add OR in case for Deutsch months.
            case "Jan":
                return dateArray[1].replace(",", "") + ".01." + dateArray[2].substring(2);
            case "Feb":
                return dateArray[1].replace(",", "") + ".02." + dateArray[2].substring(2);
            case "Mar":
                return dateArray[1].replace(",", "") + ".03." + dateArray[2].substring(2);
            case "Apr":
                return dateArray[1].replace(",", "") + ".04." + dateArray[2].substring(2);
            case "May":
                return dateArray[1].replace(",", "") + ".05." + dateArray[2].substring(2);
            case "Jun":
                return dateArray[1].replace(",", "") + ".06." + dateArray[2].substring(2);
            case "Jul":
                return dateArray[1].replace(",", "") + ".07." + dateArray[2].substring(2);
            case "Aug":
                return dateArray[1].replace(",", "") + ".08." + dateArray[2].substring(2);
            case "Sep":
                return dateArray[1].replace(",", "") + ".09." + dateArray[2].substring(2);
            case "Oct":
                return dateArray[1].replace(",", "") + ".10." + dateArray[2].substring(2);
            case "Nov":
                return dateArray[1].replace(",", "") + ".11." + dateArray[2].substring(2);
            case "Dec":
                return dateArray[1].replace(",", "") + ".12." + dateArray[2].substring(2);
            default:
                return null;
        }
    }

    /**
     * Formats the given time to a unified format.
     *
     * @param timeString - The input time.
     * @return The formatted time.
     */
    private String timeFormatter(final String timeString) {
        String[] timeArray = timeString.split(" ");
        if (timeArray[1].equals("AM")) {
            return timeArray[0];
        }
        timeArray = timeArray[0].split(":");
        if (timeArray[0].equals("12")) {
            return timeArray[0] + ":" + timeArray[1] + ":" + timeArray[2];
        }
        return Integer.toString(Integer.parseInt(timeArray[0]) + TIME_MAX_HOUR) + ":"
                + timeArray[1] + ":" + timeArray[2];
    }
}
