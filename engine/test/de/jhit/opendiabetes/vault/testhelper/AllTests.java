package de.jhit.opendiabetes.vault.testhelper;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.jhit.opendiabetes.vault.container.csv.SliceCsVEntryTest;
import de.jhit.opendiabetes.vault.data.VaultDaoTest;
import de.jhit.opendiabetes.vault.processing.DataSlicerTest;
import de.jhit.opendiabetes.vault.processing.filter.DateTimeSpanFilterTest;
import de.jhit.opendiabetes.vault.processing.filter.EventFilter;
import de.jhit.opendiabetes.vault.processing.filter.EventFilterTest;
import de.jhit.opendiabetes.vault.processing.filter.GenericFilterTest;
import de.jhit.opendiabetes.vault.processing.filter.TimePointFilterTest;
import de.jhit.opendiabetes.vault.processing.filter.TimeSpanFilterTest;

@RunWith(Suite.class)
@SuiteClasses({EventFilterTest.class, GenericFilterTest.class, TimePointFilterTest.class, DateTimeSpanFilterTest.class, TimeSpanFilterTest.class, DataSlicerTest.class, VaultDaoTest.class, SliceCsVEntryTest.class})
public class AllTests {

}
