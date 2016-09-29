package mobi.visited.util;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class MigrationManager {

    private static final Logger LOG = LoggerFactory.getLogger(MigrationManager.class);

    private final Flyway flyway;
    private final JdbcTemplate jdbcTemplate;

    public MigrationManager(
        DataSource dataSource, String migrationName, String[] locations) {
        String[] migrationLocations = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            migrationLocations[i] = migrationName + "/db/migration/" + locations[i];
        }
        jdbcTemplate = new JdbcTemplate(dataSource);
        flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations(migrationLocations);
        flyway.setSchemas(migrationName);
        flyway.setValidateOnMigrate(false);
    }

    public int migrate() {
        removeOldViews();
        int migrations = 0;
        try {
            migrations = flyway.migrate();
        } catch (FlywayException e) {
            if (e.getMessage().contains("Migration Checksum mismatch")) {
                LOG.warn("Flyway validation failed due to wrong checksum, will try to repair ...");
                flyway.repair();
                migrations = flyway.migrate();
            } else {
                throw e;
            }
        }
        return migrations;
    }

    public void clean() {
        flyway.clean();
    }

    private void removeOldViews() {
        jdbcTemplate.execute("DROP SEQUENCE IF EXISTS hibernate_sequence;");
    }
}
