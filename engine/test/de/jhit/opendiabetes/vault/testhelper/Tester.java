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
import de.jhit.opendiabetes.vault.processing.filter.FilterResult;
import de.jhit.opendiabetes.vault.processing.filter.FilterType;
import de.jhit.opendiabetes.vault.processing.filter.OverThresholdFilter;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiweGH
 */
public class Tester {

    public static void main(String[] args) {
        OverThresholdFilter fla = new OverThresholdFilter(VaultEntryType.BASAL_PROFILE, 0.5, FilterType.BASAL_AVAILABLE, FilterType.BASAL_TH);
        List<VaultEntry> vaultEntries;
        try {
            vaultEntries = StaticDataset.getStaticDataset();
            System.out.println("input: ");
            System.out.println(vaultEntries.toString());
            FilterResult flop = fla.filter(vaultEntries);
            System.out.println("output1: ");
            System.out.println(flop.filteredData.toString());
            System.out.println("output2: ");
            System.out.println(flop.timeSeries.toString());
        } catch (ParseException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
