package liuyifei.alchemists.behaviours.herbalist;

import jade.content.onto.basic.Action;
import jade.core.AID;
import liuyifei.alchemists.agents.Herbalist;
import liuyifei.alchemists.behaviours.WaitingBehaviour;
import liuyifei.alchemists.ontology.SellHerb;
import liuyifei.alchemists.ontology.SendPriceList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:10
 * Description: implement WaitingBehaviour
 */

public class HerbalistBehaviour extends WaitingBehaviour<Herbalist> {

    public HerbalistBehaviour(Herbalist myAgent) {
        super(myAgent);
    }

    @Override
    protected void action(Action action, String conversationId, AID participant) {
        if (action.getAction() instanceof SellHerb) {
            myAgent.addBehaviour(new SellHerbBehaviour(myAgent, (SellHerb) action.getAction(), conversationId, participant));
        }else if(action.getAction() instanceof SendPriceList){
            myAgent.addBehaviour(new SendPriceListBehaviour(myAgent, (SendPriceList) action.getAction(), conversationId, participant));
        }
    }
}
