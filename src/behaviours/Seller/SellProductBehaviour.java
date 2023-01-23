package behaviours.Seller;

import agents.Seller;
import behaviours.ActionBehaviour;
import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import ontology.PriceItem;
import ontology.Product;
import ontology.SellProduct;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 14:15
 * Description: sell product to student
 */

public class SellProductBehaviour extends ActionBehaviour<SellProduct, Seller> {

    public SellProductBehaviour(Seller agent, SellProduct action, String conversationId, AID participant) {
        super(agent, action, conversationId, participant);
    }

    @Override
    protected Predicate performAction() {
        for(PriceItem pi : myAgent.getSampleProduct()){
            if(pi.getProduct().getName().equals(action.getProduct().getName())){
                System.out.println(myAgent.getName() + ": I have this product - " + action.getProduct().getName() +". Give it to you, dear " + getParticipant().getName() + ".");
                action.setProduct(new Product(pi.getProduct().getName()));
                // action.setMoney(0);
            }
        }
        return new Result(action, action.getProduct());
    }
}
