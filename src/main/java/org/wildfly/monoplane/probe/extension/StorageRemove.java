package org.wildfly.monoplane.probe.extension;

import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.dmr.ModelNode;

import org.wildfly.monoplane.probe.service.ProbeService;

/**
 * Handler responsible for removing the subsystem resource from the model
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class StorageRemove extends AbstractRemoveStepHandler {

    static final StorageRemove INSTANCE = new StorageRemove();


    private StorageRemove() {
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
        //Remove any services installed by the corresponding add handler here
        context.removeService(ProbeService.SERVICE_NAME);
    }


}
