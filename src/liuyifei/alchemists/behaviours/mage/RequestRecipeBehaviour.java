package liuyifei.alchemists.behaviours.mage;

import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.behaviours.RequestActionBehaviour;
import liuyifei.alchemists.ontology.SendRecipe;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/4 20:55
 * Description: Request recipe from library
 */

public class RequestRecipeBehaviour extends RequestActionBehaviour<Mage, SendRecipe> {

    public RequestRecipeBehaviour(Mage agent, AID participant, SendRecipe action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        //getMyAgent().setBusy(true);
        System.out.println(getAgent().getName() + ": I want to search recipe for this portion - [" + this.getAction().getRecipe().getPotionName() + "].");
        return new ReceiveRecipeBehaviour(this.getMyAgent(), conversationId);
    }

}
