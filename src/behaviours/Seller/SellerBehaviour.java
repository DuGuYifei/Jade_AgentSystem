package behaviours.Seller;

import agents.Seller;
import behaviours.WaitingBehaviour;
import jade.content.onto.basic.Action;
import jade.core.AID;
import ontology.SellProduct;
import ontology.SendPriceList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 14:14
 * Description: seller wait students come
 */

public class SellerBehaviour extends WaitingBehaviour<Seller> {

    public SellerBehaviour(Seller myAgent) {
        super(myAgent);
    }

    @Override
    protected void action(Action action, String conversationId, AID participant) {
        if (action.getAction() instanceof SellProduct) {
            myAgent.addBehaviour(new SellProductBehaviour(myAgent, (SellProduct) action.getAction(), conversationId, participant));
        }else if(action.getAction() instanceof SendPriceList){
            myAgent.addBehaviour(new SendPriceListBehaviour(myAgent, (SendPriceList) action.getAction(), conversationId, participant));
        }
    }
}
