package org.wildfly.monoplane.probe.extension;

/**
 * @author Heiko Braun
 * @since 05/11/14
 */

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.MessageLogger;

/**
 * Log messages for probe module
 * @author Heiko Braun
 */
@MessageLogger(projectCode = "<<none>>")
public interface MonitorLogger extends BasicLogger {
    /**
     * A logger with the category {@code org.wildfly.monoplane.probe}.
     */
    MonitorLogger LOGGER = Logger.getMessageLogger(MonitorLogger.class, "org.wildfly.monoplane.probe");

}
