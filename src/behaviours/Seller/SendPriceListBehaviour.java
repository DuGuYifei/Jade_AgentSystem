package behaviours.Seller;

import agents.Seller;
import behaviours.ActionBehaviour;
import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import ontology.PriceItem;
import ontology.Product;
import ontology.SendPriceList;

import java.util.ArrayList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 14:15
 * Description: Send price list to student
 */

public class SendPriceListBehaviour extends ActionBehaviour<SendPriceList, Seller> {

    public SendPriceListBehaviour(Seller agent, SendPriceList action, String conversationId, AID participant) {
        super(agent, action, conversationId, participant);
    }

    @Override
    protected Predicate performAction() {
        if (myAgent.getSampleProduct().size() > 0) {
            System.out.println(myAgent.getName() + ": This is my price list, dear " + getParticipant().getName() + ".");
            // System.out.println(myAgent.getSampleProduct());
            action.getPriceList().setPriceItems(new ArrayList<>());
            for(PriceItem pi: myAgent.getSampleProduct()) {
                action.getPriceList().getPriceItems().add(pi);
                //System.out.println("\t" + pi.getProduct().getName() + "\t\t"+ pi.getPrice() + " golds");
            }
            return new Result(action, action.getPriceList());
        } else {
            //System.out.println(myAgent.getName() + ": I don't have any herb to sell. Sorry, dear " + getParticipant().getName() + ".");
            return new Result(action, myAgent.getName());
        }
    }
}
