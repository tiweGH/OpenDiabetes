/*
 * Copyright (C) 2018 a.a.aponte
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
package de.opendiabetes.vault.container;

import static de.opendiabetes.vault.container.BucketEventTriggers.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author a.a.aponte
 */
public class BucketEventTriggersTest extends Assert {

    public BucketEventTriggersTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * This test checks that all VaultEntryTypes and in a HashMap so that they
     * can be processed.
     */
    @Test
    public void testTriggerEventsSize() {
        int all_ml_rev_trigger_events = ARRAY_ENTRY_TRIGGER_HASHMAP.size();
        int ml_rev_and_one_hot_size = TRIGGER_EVENT_ACT_TIME_GIVEN.size() + TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.size()
                + TRIGGER_EVENT_ACT_TIME_ONE.size() + TRIGGER_EVENTS_NOT_YET_SET.size();
        int ml_rev_and_NOT_one_hot_size = TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.size() + TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN.size()
                + TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT.size() + TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.size()
                + TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP.size();
        assertEquals(all_ml_rev_trigger_events, (ml_rev_and_one_hot_size + ml_rev_and_NOT_one_hot_size));
    }
}
