package org.wildfly.monoplane.scheduler.storage;

import org.wildfly.monoplane.scheduler.config.Configuration;
import org.wildfly.monoplane.scheduler.diagnose.Diagnostics;

import java.util.Set;

/**
 * @author Heiko Braun
 * @since 10/10/14
 */
public interface StorageAdapter {
    void store(Set<DataPoint> datapoints);
    void setConfiguration(Configuration config);
    void setDiagnostics(Diagnostics diag);
}
