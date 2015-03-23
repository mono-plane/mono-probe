package org.wildfly.monoplane.probe.schema;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;
import org.wildfly.monoplane.probe.extension.ProbeLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * Borrowed from hawkular metrics
 */
public class SchemaManager {


    private Session session;


    public SchemaManager(Session session) {
        this.session = session;
    }

    public void dropKeyspace(String keyspace) {
        ProbeLogger.LOGGER.info("Dropping keyspace " + keyspace);

        ResultSet resultSet = session.execute("SELECT * FROM system.schema_keyspaces WHERE keyspace_name = '" +  keyspace + "'");
        if (!resultSet.isExhausted()) {
            session.execute("DROP KEYSPACE " + keyspace);
        }
    }

    public void createSchema(String keyspace) throws IOException {
        ProbeLogger.LOGGER.info("Creating schema for keyspace " + keyspace);

        ResultSet resultSet = session.execute("SELECT * FROM system.schema_keyspaces WHERE keyspace_name = '" +
                keyspace + "'");
        if (!resultSet.isExhausted()) {
            ProbeLogger.LOGGER.info("Schema already exist. Skipping schema creation.");
            return;
        }

        ImmutableMap<String, String> schemaVars = ImmutableMap.of("keyspace", keyspace);

        try (InputStream inputStream = getClass().getResourceAsStream("/schema.cql");
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            String content = CharStreams.toString(reader);

            for (String cql : content.split("(?m)^-- #.*$")) {
                if (!cql.startsWith("--")) {
                    String updatedCQL = substituteVars(cql.trim(), schemaVars);
                    ProbeLogger.LOGGER.info("Executing CQL:\n" + updatedCQL + "\n");
                    session.execute(updatedCQL);
                }
            }
        }
    }

    private String substituteVars(String cql, Map<String, String> vars) {
        try (TokenReplacingReader reader = new TokenReplacingReader(cql, vars);
             StringWriter writer = new StringWriter()) {
            char[] buffer = new char[32768];
            int cnt;
            while ((cnt = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, cnt);
            }
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to perform variable substition on CQL", e);
        }
    }

}
