package liuyifei.alchemists.behaviours.herbalist;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import liuyifei.alchemists.datacontainer.Tuple;
import liuyifei.alchemists.agents.Herbalist;
import liuyifei.alchemists.behaviours.ActionBehaviour;
import liuyifei.alchemists.ontology.Herb;
import liuyifei.alchemists.ontology.PriceItem;
import liuyifei.alchemists.ontology.SendPriceList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/5 12:22
 * Description: send price list
 */

public class SendPriceListBehaviour extends ActionBehaviour<SendPriceList, Herbalist> {

    public SendPriceListBehaviour(Herbalist agent, SendPriceList action, String conversationId, AID participant) {
        super(agent, action, conversationId, participant);
    }

    @Override
    protected Predicate performAction() {
        if (myAgent.getHerbs().size() > 0) {
            System.out.println(myAgent.getName() + ": This is my price list, dear " + getParticipant().getName() + ".");
            for(Herb h: myAgent.getHerbs().keySet()) {
                PriceItem pi = new PriceItem(new Tuple<>(h.getName(), myAgent.getHerbs().get(h), Integer.MAX_VALUE));
                action.getPriceList().addItem(pi);
                System.out.println("\t" + h.getName() + (h.getName().length() < 8? "\t\t\t\t":(h.getName().length() > 12? "\t\t" : "\t\t\t")) + myAgent.getHerbs().get(h) + " golds");
            }
            return new Result(action, action.getPriceList());
        } else {
            System.out.println(myAgent.getName() + ": I don't have any herb to sell. Sorry, dear " + getParticipant().getName() + ".");
            return new Result(action, myAgent.getName());
        }
    }
}
