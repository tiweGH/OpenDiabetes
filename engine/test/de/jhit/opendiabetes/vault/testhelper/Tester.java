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
import de.jhit.opendiabetes.vault.processing.filter.ContinuousWrapper;
import de.jhit.opendiabetes.vault.processing.filter.EventFilter;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.FilterResult;
import de.jhit.opendiabetes.vault.processing.filter.FilterType;
import de.jhit.opendiabetes.vault.processing.filter.OverThresholdFilter;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
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
            //        for (VaultEntryType v : VaultEntryType.values()) {
//            System.out.println(v.toString() + " " + v.isOneHot() + " " + v.isMLrelevant() + " " + v.mergeTo());
//        }
            Filter fla = new EventFilter(VaultEntryType.HEART_RATE_VARIABILITY);
            FilterResult fr = fla.filter(StaticDataset.getStaticDataset());
            System.out.println(fr.filteredData.size() + " " + fr.timeSeries.size());
//            for (Pair<Date, Date> tp : fr.timeSeries) {
//                System.out.println(tp.toString());
//            }
//            Date timep = TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm");
//            System.out.println(TimestampUtils.addMinutesToTimestamp(timep, -1 * (5)).toString());
//            System.out.println(new Date(timep.getTime() - MILLISECONDS.convert(5, MINUTES)).toString());
            List<Filter> lili = new ArrayList();
            lili.add(fla);
            ContinuousWrapper cont = new ContinuousWrapper(lili, 5);

            fr = cont.filter(StaticDataset.getStaticDataset());
            //List<Pair<Date, Date>> tp = cont.normalizeTimeSpans(fr.timeSeries);
            //System.out.println(tp.size());
            System.out.println(fr.size());
            System.out.println(StaticDataset.getStaticDataset().size());
            for (Pair<Date, Date> to : fr.timeSeries) {
                System.out.println(to.toString());
            }
            for (VaultEntry to : fr.filteredData) {
                System.out.println(to.toString());
            }
        } catch (ParseException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
