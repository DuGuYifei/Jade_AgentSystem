package behaviours.student;

import agents.Student;
import behaviours.ReceiveResultBehaviour;
import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import ontology.BroadcastPriceListAction;
import ontology.PriceItem;
import ontology.PriceList;
import ontology.SendPriceList;

import java.util.PriorityQueue;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 13:37
 * Description: receive price list from seller
 */

public class ReceivePriceList extends ReceiveResultBehaviour<Student> {

    public ReceivePriceList(Student agent, String conversationId) {
        super(agent, conversationId);
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        Result result = (Result) predicate;
        if(result.getAction() instanceof SendPriceList){
            PriceList priceList = (PriceList) result.getValue();
            for(PriceItem priceItem : priceList.getPriceItems()){
                getMyAgent().getPriceMap().putIfAbsent(priceItem.getProduct().getName(), new PriorityQueue<>());
                getMyAgent().getPriceMap().get(priceItem.getProduct().getName()).add(priceItem);
            }

            getMyAgent().addBehaviour(new BroadcastPriceList(getMyAgent(), new BroadcastPriceListAction((SendPriceList) result.getAction())));

            System.out.println("Broadcasting: " + getMyAgent().getName() + ": I got price list from [" + priceList.getSellerAid().getName()+ "]");

            getMyAgent().getPriceTaskMap().remove((SendPriceList) result.getAction());

            getMyAgent().setBusy(false);
        }
    }


}
