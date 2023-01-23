package behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import lombok.extern.java.Log;

import java.util.logging.Level;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:00
 * Description: zarejestrowanie usługi w katalogu 在目录中注册服务
 */

@Log
public class RegisterServiceBehaviour extends OneShotBehaviour {

    private final String serviceType;

    public RegisterServiceBehaviour(Agent agent, String serviceType) {
        super(agent);
        this.serviceType = serviceType;
    }

    @Override
    public void action() {
        DFAgentDescription dfad = new DFAgentDescription();
        dfad.setName(myAgent.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        sd.setName(myAgent.getName() + "-" + serviceType);
        dfad.addServices(sd);
        try {
            DFService.register(myAgent, dfad);
        } catch (FIPAException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

}
