package org.wildfly.monoplane.scheduler;

/**
 * @author Heiko Braun
 * @since 05/11/14
 */

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.MessageLogger;

/**
 * Log messages for probe scheduler component
 * @author Heiko Braun
 */
@MessageLogger(projectCode = "<<none>>")
public interface SchedulerLogger extends BasicLogger {
    /**
     * A logger with the category {@code org.wildfly.monoplane.scheduler}.
     */
    SchedulerLogger LOGGER = Logger.getMessageLogger(SchedulerLogger.class, "org.wildfly.monoplane.scheduler");

}
