/*
 * Copyright (C) 2018 Daniel
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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.Date;

/**
 *
 * @author Daniel
 * 
 * This Filter only allows events if they have been happend after a given Time and if the EventType is correct.
 */
public class MarginAfterEventFilter extends Filter{

    private final long marginAfter;
    private VaultEntryType vaultEntryType;
    private Date lastEntryTime = null;

    /**
     * 
     * @param marginAfter; This is the Time after the next Event will be allowed
     * @param vaultEntryType; This the the Type to check
     */
    public MarginAfterEventFilter(long marginAfter, VaultEntryType vaultEntryType) {
        this.marginAfter = marginAfter;    
        this.vaultEntryType = vaultEntryType;
    }

    MarginAfterEventFilter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    FilterType getType() {
        return FilterType.CONTINOUS_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = false;
        if(vaultEntryType.equals(entry.getType()))
        {
            if(lastEntryTime == null)
            {
                result = true;
                lastEntryTime = entry.getTimestamp();
            }
            else if(TimestampUtils.addMinutesToTimestamp(lastEntryTime, marginAfter).before(entry.getTimestamp()))
            {
                result = true;
                lastEntryTime = entry.getTimestamp();
            }
            
        }       
        
        return result;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new MarginAfterEventFilter(marginAfter, vaultEntry.getType());
    }
    
}
