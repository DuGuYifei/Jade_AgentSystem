package liuyifei.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import liuyifei.alchemists.agents.Herbalist;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ActionBehaviour;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.behaviours.RequestActionBehaviour;
import liuyifei.alchemists.ontology.BorrowGold;
import liuyifei.alchemists.ontology.SendPriceList;
import liuyifei.alchemists.ontology.SendRecipe;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/3 9:46
 * Description: send money to othe mage
 */

public class SendMoney extends ActionBehaviour<BorrowGold, Mage> {

    public SendMoney(Mage agent, BorrowGold action, String conversationId, AID participant) {
        super(agent, action, conversationId, participant);
    }

    @Override
    protected Predicate performAction() {
        System.out.println(getAgent().getName() + ": give [" + getParticipant().getName() + "] " + action.getGold().getGold() + " golds.");
        return new Result(action, action.getGold());
    }
}
