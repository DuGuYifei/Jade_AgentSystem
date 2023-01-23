package liuyifei.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.ontology.BorrowGold;
import liuyifei.alchemists.ontology.Gold;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/3 9:30
 * Description: Receive money from other mage
 */

public class ReceiveMoney extends ReceiveResultBehaviour<Mage> {
    public ReceiveMoney(Mage agent, String conversationId) {
        super(agent, conversationId);
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        Result result = (Result) predicate;
        if (result.getValue() instanceof Gold && result.getAction() instanceof BorrowGold) {
            //System.out.println("-------------------before: gold " + getMyAgent().getGold().getGold());
            getMyAgent().getGold().setGold(getMyAgent().getGold().getGold() + ((Gold) result.getValue()).getGold());
            System.out.println(getMyAgent().getName() + ": I got " + ((Gold) result.getValue()).getGold() + " gold from " + participant.getName());
            //System.out.println("-------------------after: gold " + getMyAgent().getGold().getGold());
        }
    }
}
