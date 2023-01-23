package liuyifei.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import liuyifei.alchemists.datacontainer.Tuple;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.ontology.PriceItem;
import liuyifei.alchemists.ontology.PriceList;

import java.awt.image.ConvolveOp;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/5 11:36
 * Description: receive price list from agents
 */

public class ReceivePriceListBehaviour extends ReceiveResultBehaviour<Mage> {

    private boolean sendToMage = false;
    private AID originSender;

    public ReceivePriceListBehaviour(Mage agent, String conversationId) {
        super(agent, conversationId);
    }

    public ReceivePriceListBehaviour(Mage agent, String conversationId, AID originSender, boolean sendToMage) {
        super(agent, conversationId);
        this.sendToMage = sendToMage;
        this.originSender = originSender;
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        getMyAgent().setBusy(false);
        if(sendToMage){
            System.out.println("\n-------" + getAgent().getName() + ": I have gotten price list from [" + participant.getName() + "], now send to task-sender to handle\n");
            getAgent().addBehaviour(new SendPriceListBehaviour((Mage)getAgent(), predicate, getConversationId(), originSender));
        }else {
            Result result = (Result) predicate;
            if (result.getValue() instanceof String) {
                System.out.println(getAgent().getName() + ": There is no inventory of herbs from - [" + result.getValue() + "].\n");
            } else {
                PriceList p = (PriceList) result.getValue();
                HashMap<String, PriorityQueue<Tuple<AID>>> priceMap = ((Mage) getAgent()).getPriceMap();

                //       Herb,    Price,   Amount
                for (PriceItem t : p.getPrices()) {
                    if (priceMap.containsKey(t.getItem().getX())) {
                        priceMap.get(t.getItem().getX()).add(new Tuple<>(p.getAid(), t.getItem().getY(), t.getItem().getZ()));
                    } else {
                        PriorityQueue<Tuple<AID>> pq = new PriorityQueue<>();
                        pq.add(new Tuple<>(p.getAid(), t.getItem().getY(), t.getItem().getZ()));
                        priceMap.put(t.getItem().getX(), pq);
                    }
                }

                System.out.println(getAgent().getName() + ": I got price list from - [" + p.getAid().getName() + "].\n");
            }
        }
    }
}
