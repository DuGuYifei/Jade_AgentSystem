package liuyifei.alchemists.behaviours.alchemist;

import jade.content.onto.basic.Action;
import jade.core.AID;
import liuyifei.alchemists.agents.Alchemist;
import liuyifei.alchemists.behaviours.WaitingBehaviour;
import liuyifei.alchemists.ontology.SellPotion;
import liuyifei.alchemists.ontology.SendPriceList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:07
 * Description: implement WaitingBehaviour
 */

public class AlchemistBehaviour extends WaitingBehaviour<Alchemist> {

    public AlchemistBehaviour(Alchemist agent) {
        super(agent);
    }

    @Override
    protected void action(Action action, String conversationId, AID participant) {
        if (action.getAction() instanceof SellPotion) {
            myAgent.addBehaviour(new SellPotionBehaviour(myAgent, (SellPotion) action.getAction(), conversationId, participant));
        }else if(action.getAction() instanceof SendPriceList){
            myAgent.addBehaviour(new SendPriceListBehaviour(myAgent, (SendPriceList) action.getAction(), conversationId, participant));
        }
    }

}