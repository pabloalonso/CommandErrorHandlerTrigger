package org.bonitasoft;

import org.bonitasoft.engine.command.PlatformCommand;
import org.bonitasoft.engine.command.SCommandExecutionException;
import org.bonitasoft.engine.command.SCommandParameterizationException;
import org.bonitasoft.engine.command.TenantCommand;
import org.bonitasoft.engine.incident.Incident;
import org.bonitasoft.engine.incident.IncidentService;
import org.bonitasoft.engine.service.PlatformServiceAccessor;
import org.bonitasoft.engine.service.TenantServiceAccessor;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by pablo on 12/12/2016.
 * org.bonitasoft.ErrorCommand
 */

public class ErrorCommand extends TenantCommand{
    public Serializable execute(Map<String, Serializable> map, TenantServiceAccessor tenantServiceAccessor) throws SCommandParameterizationException, SCommandExecutionException {
        try {

            Long flowNodeId = (Long) map.get("flowNodeId");
            final IncidentService incidentService = tenantServiceAccessor.getIncidentService();
            final Incident incident = new Incident("", "Procedure to recover: call processApi.executeFlowNode("+flowNodeId+")", new Exception(), new Exception());
            incidentService.report(tenantServiceAccessor.getTenantId(), incident);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return "Done";


}
}
