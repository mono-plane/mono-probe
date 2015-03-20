package org.wildfly.monoplane.probe.extension;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.RestartParentWriteAttributeHandler;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceName;
import org.wildfly.monoplane.probe.service.ProbeService;

/**
 * Handler that restarts the service on attribute changes
 * @author Heiko W. Rupp
 */
public class InputWriteAttributeHandler extends RestartParentWriteAttributeHandler {

    public InputWriteAttributeHandler(AttributeDefinition... definitions) {
        super(InputDefinition.DATA_INPUT, definitions);
    }

    @Override
    protected void recreateParentService(
            OperationContext context, PathAddress parentAddress, ModelNode parentModel,
            ServiceVerificationHandler verificationHandler) throws OperationFailedException {

    }

    @Override
    protected ServiceName getParentServiceName(PathAddress parentAddress) {
        return ProbeService.SERVICE_NAME.append(parentAddress.getLastElement().getValue());
    }
}
