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
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.FilterResult;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

            long minutes = (workingSet.get(workingSet.size() - 1).getTimestamp().getTime() - workingSet.get(0).getTimestamp().getTime()) / (60 * 1000);
            System.out.println("Test_end_output_minutes_:");
            System.out.println(minutes);
            System.out.println(TimestampUtils.createCleanTimestamp("2017.10.08-00:00", "yyyy.MM.dd-HH:mm").getTime());
            System.out.println(TimestampUtils.createCleanTimestamp("2017.11.08-00:00", "yyyy.MM.dd-HH:mm").getTime());
            System.out.println(new Date(1510095600000L).toString());
            System.out.println(VaultEntryType.valueOf("GLUCOSE_CGM_ALERT").isOneHot());

        } catch (ParseException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
