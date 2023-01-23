package liuyifei.alchemists.behaviours.mage;

import jade.core.AID;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.behaviours.RequestActionBehaviour;
import liuyifei.alchemists.ontology.BorrowGold;
import liuyifei.alchemists.ontology.SellPotion;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/3 9:28
 * Description: Borrow Money from other mage
 */

public class RequestMoney extends RequestActionBehaviour<Mage, BorrowGold> {
    public RequestMoney(Mage agent, AID participant, BorrowGold action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        System.out.println("----------\n" + getAgent().getName() + ": I still don't have enough money. I need " + getAction().getGold() + " gold from " + getParticipant().getName() + ".\n----------");
        return new ReceiveMoney(getMyAgent(), conversationId);
    }
}
