package org.wildfly.monoplane.probe.extension;


import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.wildfly.monoplane.probe.service.ProbeService;

import java.util.List;

/**
 * Handler responsible for adding the subsystem resource to the model.
 * In fact there is nothing to do here.
 *
 * Created by the archetype.
 * @author Heiko Braun
 */
class SubsystemAdd extends AbstractAddStepHandler {

    static final SubsystemAdd INSTANCE = new SubsystemAdd();

    private SubsystemAdd() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        model.setEmptyObject();
    }


    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {

        ModelNode subsystemConfig = Resource.Tools.readModel(context.readResource(PathAddress.EMPTY_ADDRESS));

        // TODO: the root resource doesn't inlcude the runtime paramters, hence we cannot depict the host/server name & launch-type from it.
        //ModelNode systemConfig = Resource.Tools.readModel(context.readResourceFromRoot(PathAddress.EMPTY_ADDRESS, false));

        // Add the service
        newControllers.add(
                ProbeService.createService(
                        context.getServiceTarget(),
                        verificationHandler,
                        subsystemConfig
                )
        );
    }
}
