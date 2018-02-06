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
package de.jhit.opendiabetes.vault.util;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.container.VaultEntryTypeGroup;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author tiweGH
 */
public class VaultEntryUtils {

    /**
     * Returns the VaultEntry with the timestamp located in the middle of the
     * given entry-timestamps
     *
     * @param data contains sorted entries with timestamps, first with the
     * oldest and last with the newest timestamp
     * @return an entry with a timestamp nearest the actual middle of the first
     * and the last timestamp
     */
    public static VaultEntry getNearestMidEntry(List<VaultEntry> data) {
        VaultEntry result = null;
        if (data != null) {
            long actualMid = TimestampUtils.getMidDate(data.get(0).getTimestamp(), data.get(data.size() - 1).getTimestamp()).getTime();
            long currentMin = Long.MAX_VALUE;
            long temp;
            for (VaultEntry vaultEntry : data) {
                temp = Math.abs(actualMid - vaultEntry.getTimestamp().getTime());
                if (temp < currentMin) {
                    result = vaultEntry;
                    currentMin = temp;
                }
            }
        }
        return result;
    }

    public static boolean equalsTypeAndGroup(VaultEntry entry, VaultEntryType type, VaultEntryTypeGroup group) {
        boolean result;
        result = (type == null || entry.getType() == type) && (group == null || entry.getType().getGroup() == group);
        return result;
    }

    public static List<VaultEntry> sort(List<VaultEntry> data) {
        data.sort(new SortVaultEntryByDate());
        return data;
    }
}
