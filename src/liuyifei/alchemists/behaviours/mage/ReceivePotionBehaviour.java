package liuyifei.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.ontology.Potion;
import liuyifei.alchemists.ontology.SellPotion;

import java.util.ArrayList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:14
 * Description: implement ReceiveResultBehaviour, odebranie mikstury 接收药水
 */

public class ReceivePotionBehaviour extends ReceiveResultBehaviour<Mage> {


    public ReceivePotionBehaviour(Mage agent, String conversationId) {
        super(agent, conversationId);
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        getMyAgent().setBusy(false);
        Result result = (Result) predicate;
        if (result.getValue() instanceof String) {
            ((Mage) getAgent()).getFailBuyList().add((String) result.getValue());
            System.out.println(getAgent().getName() + ": There is no one sell the potion - " + result.getValue() + " - I want to buy.");
        } else {
            SellPotion p = (SellPotion) result.getValue();
            getMyAgent().getPotionList().add(p.getPotion());
            System.out.println(getAgent().getName() + ": I got and add this potion - [" + p.getPotion().getName() + "] - to my potion bag.\n");
            ((Mage) getAgent()).getHerbList().clear();
            if(p.getHerbList() != null)
                ((Mage) getAgent()).getHerbList().addAll(p.getHerbList());
            //((Mage) getAgent()).getLackHerbList().clear();
            //if(p.getLackHerbList() != null)
               // ((Mage) getAgent()).getLackHerbList().addAll(p.getLackHerbList());
        }
    }
}
