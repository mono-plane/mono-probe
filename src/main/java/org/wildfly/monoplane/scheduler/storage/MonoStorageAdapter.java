package org.wildfly.monoplane.scheduler.storage;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.hawkular.metrics.core.api.MetricId;
import org.hawkular.metrics.core.api.NumericData;
import org.hawkular.metrics.core.api.NumericMetric;
import org.hawkular.metrics.core.impl.cassandra.DataAccessImpl;
import org.hawkular.metrics.core.impl.cassandra.MetricsServiceCassandra;
import org.wildfly.monoplane.probe.extension.ProbeLogger;
import org.wildfly.monoplane.probe.schema.SchemaManager;
import org.wildfly.monoplane.scheduler.config.Configuration;
import org.wildfly.monoplane.scheduler.diagnose.Diagnostics;

import java.util.Set;

/**
 * @author Heiko Braun
 * @since 23/03/15
 */
public class MonoStorageAdapter implements StorageAdapter {

    private final DefaultKeyResolution keyResolution;
    private Cluster cluster;
    private Configuration config;
    private Diagnostics diagnostics;
    private Session session;
    private DataAccessImpl dataAccess;


    public MonoStorageAdapter() {
        this.keyResolution = new DefaultKeyResolution();
    }

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

        dataAccess = new DataAccessImpl(session);
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

            // TODO: improve API to retrieve keys
            String key = keyResolution.resolve(datapoints.iterator().next().getTask());

            NumericMetric metric = new NumericMetric(new MetricId(key));

            for (DataPoint datapoint : datapoints) {
                metric.addData(new NumericData(datapoint.getTimestamp(), datapoint.getValue()));
            }

            // TODO clarification: use service or dao ?
            ResultSetFuture future = dataAccess.insertData(metric, MetricsServiceCassandra.DEFAULT_TTL);
            Futures.addCallback(future, new FutureCallback<ResultSet>() {
                @Override
                public void onSuccess(ResultSet result) {

                }

                @Override
                public void onFailure(Throwable t) {
                    ProbeLogger.LOGGER.error("Failed to persist data", t);
                    diagnostics.getStorageErrorRate().mark(1);
                }
            });



        } catch (Throwable t) {
            ProbeLogger.LOGGER.error("Failed to persist data", t);
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
