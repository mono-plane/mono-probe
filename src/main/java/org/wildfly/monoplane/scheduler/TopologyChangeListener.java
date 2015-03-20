package org.wildfly.monoplane.scheduler;

/**
 * The service rebuilds the task list when the topology of a domain changes.
 *
 * @author Heiko Braun
 * @since 10/10/14
 */
public interface TopologyChangeListener {
    void onChange();
}
