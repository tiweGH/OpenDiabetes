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
package de.jhit.opendiabetes.vault.data;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.HsqldbDatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.logger.Log;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.jhit.opendiabetes.vault.container.RawEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public class VaultDao {

    public static final long RESULT_ERROR = -1;
    private static final String DATABASE_URL = "jdbc:hsqldb:mem:odvault";
    //private static final String DATABASE_URL = "jdbc:hsqldb:file:./test.db";
    private static final Logger LOG = Logger.getLogger(VaultDao.class.getName());
    private static VaultDao INSTANCE = null;

    private ConnectionSource connectionSource;
    private Dao<VaultEntry, Long> vaultDao;
    private Dao<RawEntry, Long> rawDao;

    private VaultDao() {
    }

    public static VaultDao getInstance() {
        if (INSTANCE == null) {
            LOG.severe("Database is not initialized. Call VaultDao.initializeDb first!");
            System.exit(-1);
        }
        return INSTANCE;
    }

    public static void finalizeDb() throws IOException {
        INSTANCE.connectionSource.close();
    }

    public static void initializeDb() throws SQLException {
        //TODO combine logging
        System.setProperty(LoggerFactory.LOG_TYPE_SYSTEM_PROPERTY,
                LoggerFactory.LogType.LOCAL.toString());
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY,
                Log.Level.INFO.toString());

        INSTANCE = new VaultDao();
        INSTANCE.initDb();
    }

    private void initDb() throws SQLException {
        // create a connection source to our database
        connectionSource = new JdbcConnectionSource(DATABASE_URL, "sa", "",
                new HsqldbDatabaseType());
        // instantiate the DAO 
        vaultDao = DaoManager.createDao(connectionSource, VaultEntry.class);
        if (!vaultDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, VaultEntry.class);
        } else {
            LOG.warning("Found existing DB for VaultEntries. Reusing it!!");
        }

        rawDao = DaoManager.createDao(connectionSource, RawEntry.class);
        if (!rawDao.isTableExists()) {
            TableUtils.createTableIfNotExists(connectionSource, RawEntry.class);
        }
//        TableUtils.createTableIfNotExists(connectionSource, SliceEntry.class);
    }

    /**
     *
     * @param entry
     * @return id of respective entry or RESULT_ERROR
     */
    public long putEntry(VaultEntry entry) {
        try {
            return vaultDao.createIfNotExists(entry).getId();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error saving entry:\n" + entry.toString(), ex);
            return RESULT_ERROR;
        }
    }

    public long putRawEntry(RawEntry entry) {
        // TODO rethink raw entry tracking
        return 0;
//        try {
//            return rawDao.createIfNotExists(entry).getId();
//        } catch (SQLException ex) {
//            LOG.log(Level.SEVERE, "Error saving entry:\n" + entry.toString(), ex);
//            return RESULT_ERROR;
//        }
    }

    public boolean removeDublicates() {
        // DELETE FROM MyTable WHERE RowId NOT IN (SELECT MIN(RowId) FROM MyTable GROUP BY Col1, Col2, Col3);
        // but we need a workaround for the or mapper
        try {
            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", true)
                    .prepare();
            CloseableIterator<VaultEntry> iterator = vaultDao.iterator(query);

            Date startGenerationTimestamp = null;
            List<VaultEntry> tmpList = new ArrayList<>();
            List<Long> dublicateId = new ArrayList<>();
            while (iterator.hasNext()) {
                VaultEntry entry = iterator.next();
                if (startGenerationTimestamp == null) {
                    // start up
                    startGenerationTimestamp = entry.getTimestamp();
                    tmpList.add(entry);
                } else if (!startGenerationTimestamp
                        .equals(entry.getTimestamp())) {
                    // not same timestamp --> new line generation
                    startGenerationTimestamp = entry.getTimestamp();
                    tmpList.clear();
                    tmpList.add(entry);
                } else {
                    // same timestamp --> check if it is a dublicate
                    for (VaultEntry item : tmpList) {
                        if (item.equals(entry)) {
                            // dublicate --> delete and move on
                            dublicateId.add(entry.getId());
                            break;
                        }
                    }
                }
            }

            // delete dublicates
            int lines = vaultDao.deleteIds(dublicateId);
            LOG.log(Level.INFO, "Removed {0} dublicates", lines);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
            return false;
        }

        return true;
    }

    public List<VaultEntry> queryGlucoseBetween(Date from, Date to) {
        List<VaultEntry> returnValues = null;
        try {
            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", true)
                    .where()
                    .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.GLUCOSE_BG)
                    .or()
                    .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.GLUCOSE_CGM)
                    .or()
                    .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.GLUCOSE_CGM_ALERT)
                    .and()
                    .between(VaultEntry.TIMESTAMP_FIELD_NAME, from, to)
                    .prepare();
            returnValues = vaultDao.query(query);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
        }
        return returnValues;
    }

    public List<VaultEntry> queryExerciseBetween(Date from, Date to) {
        List<VaultEntry> returnValues = null;
        try {
            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", true)
                    .where()
                    .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.EXERCISE_BICYCLE)
                    .or()
                    .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.EXERCISE_RUN)
                    .or()
                    .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.EXERCISE_WALK)
                    .or()
                    .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.EXERCISE_MANUAL)
                    .and()
                    .between(VaultEntry.TIMESTAMP_FIELD_NAME, from, to)
                    .prepare();
            returnValues = vaultDao.query(query);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
        }
        return returnValues;
    }

    public VaultEntry queryLatestEventBefore(Date timestamp, VaultEntryType type) {
        VaultEntry returnValue = null;
        try {

            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", false)
                    .limit(1L)
                    .where()
                    .eq(VaultEntry.TYPE_FIELD_NAME, type)
                    .and()
                    .le(VaultEntry.TIMESTAMP_FIELD_NAME, timestamp)
                    .prepare();
            List<VaultEntry> tmpList = vaultDao.query(query);
            if (tmpList.size() > 0) {
                returnValue = tmpList.get(0);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
        }
        return returnValue;
    }

    public List<VaultEntry> queryAllVaultEntrys() {
        List<VaultEntry> returnValues = new ArrayList<>();
        try {

            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", true)
                    .prepare();
            returnValues = vaultDao.query(query);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
        }
        return returnValues;
    }

    /**
     *
     * @param id
     * @return vault entry with respective id or null
     */
    public VaultEntry queryVaultEntryById(long id) {
        List<VaultEntry> returnValues = new ArrayList<>();
        try {
            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", true)
                    .limit(1l)
                    .where()
                    .eq(VaultEntry.ID_FIELD_NAME, id)
                    .prepare();
            returnValues = vaultDao.query(query);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
            return null;
        }
        return returnValues.get(0);
    }

    public List<VaultEntry> queryVaultEntrysBetween(Date from, Date to) {
        List<VaultEntry> returnValues = new ArrayList<>();
        try {
            Date fromTimestamp = TimestampUtils.createCleanTimestamp(from);
            Date toTimestamp = TimestampUtils.createCleanTimestamp(to);

            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", true)
                    .where()
                    .between(VaultEntry.TIMESTAMP_FIELD_NAME, fromTimestamp, toTimestamp)
                    .prepare();
            returnValues = vaultDao.query(query);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
        }
        return returnValues;
    }

    public List<VaultEntry> queryBasalBetween(Date from, Date to) {
        List<VaultEntry> returnValues = null;
        try {
            PreparedQuery<VaultEntry> query
                    = vaultDao.queryBuilder().orderBy("timestamp", true)
                    .where()
                    .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.BASAL_MANUAL)
                    .or()
                    .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.BASAL_PROFILE)
                    .or()
                    .eq(VaultEntry.TYPE_FIELD_NAME, VaultEntryType.BASAL_INTERPRETER)
                    .and()
                    .between(VaultEntry.TIMESTAMP_FIELD_NAME, from, to)
                    .prepare();
            returnValues = vaultDao.query(query);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
        }
        return returnValues;
    }

    public boolean removeEntry(VaultEntry historyEntry) {
        try {
            vaultDao.deleteById(historyEntry.getId());
            LOG.log(Level.INFO, "Removed dntry: {0}", historyEntry.toString());
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error while db query", ex);
            return false;
        }

        return true;
    }
}
