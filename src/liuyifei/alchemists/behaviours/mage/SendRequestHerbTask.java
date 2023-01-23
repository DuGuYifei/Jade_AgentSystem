package liuyifei.alchemists.behaviours.mage;

import jade.core.AID;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.behaviours.RequestActionBehaviour;
import liuyifei.alchemists.ontology.RequestHerbTask;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/3 11:39
 * Description: Send buy herb task to other mage
 */

public class SendRequestHerbTask extends RequestActionBehaviour<Mage, RequestHerbTask> {

    public SendRequestHerbTask(Mage agent, AID participant, RequestHerbTask action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        getMyAgent().getGold().setGold(getMyAgent().getGold().getGold() - getAction().getGold().getGold());
        System.out.println(getAgent().getName() + ": divide buy herb task to " + getParticipant().getName() + ".\n");
        return new ReceiveHerbBehaviour(this.getMyAgent(), conversationId);
    }
}
