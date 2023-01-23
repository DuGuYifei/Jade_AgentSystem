package liuyifei.alchemists.behaviours.mage;

import jade.core.AID;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.behaviours.RequestActionBehaviour;
import liuyifei.alchemists.ontology.RequestPriceTask;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/2 4:04
 * Description: Send request price task to mage
 */

public class SendRequestPriceTask extends RequestActionBehaviour<Mage, RequestPriceTask> {

    public SendRequestPriceTask(Mage agent, AID participant, RequestPriceTask action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        System.out.println(getAgent().getName() + ": divide request price task to " + getParticipant().getName() + ".\n");
        return new ReceivePriceListBehaviour(this.getMyAgent(), conversationId);
    }
}
