package liuyifei.alchemists.behaviours.mage;

import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.behaviours.RequestActionBehaviour;
import liuyifei.alchemists.ontology.SendPriceList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/5 1:32
 * Description: evaluate the price of herbs in the market
 */

public class RequestPriceBehaviour extends RequestActionBehaviour<Mage, SendPriceList> {

    private boolean sendToMage = false;
    private AID originSender;

    public RequestPriceBehaviour(Mage agent, AID participant, SendPriceList action) {
        super(agent, participant, action);
    }

    public RequestPriceBehaviour(Mage agent, AID participant, SendPriceList action, String conversationId, AID originSender, boolean sendToMage) {
        super(agent, participant, action, conversationId);
        this.sendToMage = sendToMage;
        this.originSender = originSender;
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        getMyAgent().setBusy(true);
        System.out.println(getAgent().getName() + ": What herbs you have and how much?");
        if(sendToMage){
            return new ReceivePriceListBehaviour(getMyAgent(), conversationId, originSender, true);
        }else {
            return new ReceivePriceListBehaviour(this.getMyAgent(), conversationId);
        }
    }

}
