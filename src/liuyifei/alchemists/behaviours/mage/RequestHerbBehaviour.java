package liuyifei.alchemists.behaviours.mage;

import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.behaviours.RequestActionBehaviour;
import liuyifei.alchemists.ontology.SellHerb;
import liuyifei.alchemists.ontology.SendPriceList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:14
 * Description: implement RequestActionBehaviour, żądanie zioła, 药草请求
 */

public class RequestHerbBehaviour extends RequestActionBehaviour<Mage, SellHerb> {

    private boolean sendToMage = false;
    private AID originSender;

    public RequestHerbBehaviour(Mage agent, AID participant, SellHerb action) {
        super(agent, participant, action);
    }

    public RequestHerbBehaviour(Mage agent, AID participant, SellHerb action, String conversationId, AID originSender, boolean sendToMage) {
        super(agent, participant, action, conversationId);
        this.sendToMage = sendToMage;
        this.originSender = originSender;
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        getMyAgent().setBusy(true);
        getMyAgent().getGold().setGold(getMyAgent().getGold().getGold() - getAction().getMoney());
        System.out.println(getAgent().getName() + ": I want this herb - " + this.getAction().getHerb().getName() + ".");
        if(sendToMage){
            return new ReceiveHerbBehaviour(this.getMyAgent(), conversationId, originSender, true);
        }else {
            return new ReceiveHerbBehaviour(this.getMyAgent(), conversationId);
        }
    }

}
