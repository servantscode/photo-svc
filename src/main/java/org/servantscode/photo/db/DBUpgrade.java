package org.servantscode.photo.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servantscode.commons.db.AbstractDBUpgrade;

import java.sql.SQLException;

public class DBUpgrade extends AbstractDBUpgrade {
    private static final Logger LOG = LogManager.getLogger(DBUpgrade.class);

    @Override
    public void doUpgrade() throws SQLException {
        LOG.info("Verifying database structures.");

        if(!tableExists("photos")) {
            LOG.info("-- Creating photos table");
            runSql("CREATE TABLE photos (guid TEXT PRIMARY KEY, " +
                                        "filetype TEXT, " +
                                        "bytes BYTEA, " +
                                        "public BOOLEAN, " +
                                        "org_id INTEGER references organizations(id) ON DELETE CASCADE)");
        }
    }
}
