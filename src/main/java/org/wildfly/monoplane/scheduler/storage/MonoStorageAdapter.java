package org.wildfly.monoplane.scheduler.storage;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import org.wildfly.monoplane.probe.extension.ProbeLogger;
import org.wildfly.monoplane.probe.schema.SchemaManager;
import org.wildfly.monoplane.scheduler.config.Configuration;
import org.wildfly.monoplane.scheduler.diagnose.Diagnostics;

import java.io.IOException;
import java.util.Set;

/**
 * @author Heiko Braun
 * @since 23/03/15
 */
public class MonoStorageAdapter implements StorageAdapter {

    private Cluster cluster;
    private Configuration config;
    private Diagnostics diagnostics;
    private Session session;

    @Override
    public void start() {

        String keyspace = config.getStorageDBName();
        String storageUrl = config.getStorageUrl();
        String ip = storageUrl.substring(7, storageUrl.length()); // TODO fixme, it assumes 'http://'

        cluster = Cluster.builder()
                .addContactPoint(ip)
                .build();
        Metadata metadata = cluster.getMetadata();
        ProbeLogger.LOGGER.infof("Connected to cluster: %s\n", metadata.getClusterName());

        for ( Host host : metadata.getAllHosts() )
        {
            ProbeLogger.LOGGER.infof(
                    "Datacenter: %s; Host: %s; Rack: %s\n",
                    host.getDatacenter(),
                    host.getAddress(),
                    host.getRack()
            );

        }


        // force drop/create of schema
        // TODO: more subtle, configurable settings
        try {
            Session sess = cluster.connect();
            SchemaManager schemaManager = new SchemaManager(sess);
            schemaManager.dropKeyspace(keyspace);
            schemaManager.createSchema(keyspace);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to initialize schema", t);
        }

        this.session = cluster.connect(config.getStorageDBName());
    }

    @Override
    public void stop() {

        if(session!=null)
            session.close();

        if(cluster!=null)
            cluster.close();


    }

    @Override
    public void store(Set<DataPoint> datapoints) {

        try {
            // TODO

            /*session.execute("CREATE KEYSPACE IF NOT EXISTS simplex WITH replication " +
                  "= {'class':'SimpleStrategy', 'replication_factor':3};");*/

        } catch (Throwable t) {
            t.printStackTrace();
            diagnostics.getStorageErrorRate().mark(1);
        }

    }

    @Override
    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    @Override
    public void setDiagnostics(Diagnostics diag) {
        this.diagnostics = diag;
    }

}
