package liuyifei.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.core.AID;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ActionBehaviour;
import liuyifei.alchemists.ontology.SellHerb;
import liuyifei.alchemists.ontology.SendPriceList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/3 11:49
 * Description: send herb to mage from mage
 */

public class SendHerbBehaviour extends ActionBehaviour<SellHerb, Mage> {

    private final Predicate predicate;

    public SendHerbBehaviour(Mage agent, Predicate predicate, String conversationId, AID participant) {
        super(agent, conversationId, participant);
        this.predicate = predicate;
    }

    @Override
    protected Predicate performAction() {
        return predicate;
    }
}
