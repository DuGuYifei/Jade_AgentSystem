package liuyifei.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ActionBehaviour;
import liuyifei.alchemists.ontology.SendPriceList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/1 12:10
 * Description: Send price back to mage
 */

public class SendPriceListBehaviour extends ActionBehaviour<SendPriceList, Mage> {

    private final Predicate predicate;

    public SendPriceListBehaviour(Mage agent, Predicate predicate, String conversationId, AID participant) {
        super(agent, conversationId, participant);
        this.predicate = predicate;
    }

    @Override
    protected Predicate performAction() {
        return predicate;
    }
}
