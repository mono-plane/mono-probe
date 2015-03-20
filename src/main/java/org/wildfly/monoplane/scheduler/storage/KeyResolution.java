package org.wildfly.monoplane.scheduler.storage;

import org.wildfly.monoplane.scheduler.polling.Task;

/**
 * @author Heiko Braun
 * @since 24/10/14
 */
public interface KeyResolution {
    String resolve(Task task);
}
