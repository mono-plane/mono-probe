package org.wildfly.monoplane.probe.extension;

import java.util.Arrays;
import java.util.Collection;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.PersistentResourceDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelType;

/**
 * Definition of a diagnostics handler
 * @author Heiko W. Rupp
 */
public class DiagnosticsDefinition extends PersistentResourceDefinition {

    public static final DiagnosticsDefinition INSTANCE = new DiagnosticsDefinition();

    public static final String DIAGNOSTICS = "diagnostics";

    static final SimpleAttributeDefinition ENABLED  = new SimpleAttributeDefinitionBuilder("enabled", ModelType.BOOLEAN,false)
        .build();

    static final SimpleAttributeDefinition SECONDS  = new SimpleAttributeDefinitionBuilder("seconds", ModelType.INT,true)
            .build();

    static final SimpleAttributeDefinition MINUTES  = new SimpleAttributeDefinitionBuilder("minutes", ModelType.INT,true)
            .build();

    static AttributeDefinition[] ATTRIBUTES = {
            ENABLED, SECONDS, MINUTES
    };

    private DiagnosticsDefinition() {
        super(PathElement.pathElement(DIAGNOSTICS),
            SubsystemExtension.getResourceDescriptionResolver(DIAGNOSTICS),
            DiagnosticsAdd.INSTANCE,
            DiagnosticsRemove.INSTANCE
            );
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        DiagnosticsWriteAttributeHandler handler = new DiagnosticsWriteAttributeHandler(ATTRIBUTES);

        for (AttributeDefinition attr : ATTRIBUTES) {
            resourceRegistration.registerReadWriteAttribute(attr, null, handler);
        }

    }

    @Override
    public Collection<AttributeDefinition> getAttributes() {
        return Arrays.asList(ATTRIBUTES);
    }
}
