package liuyifei.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.datacontainer.Pair;
import liuyifei.alchemists.ontology.HerbItem;
import liuyifei.alchemists.ontology.SellHerb;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:11
 * Description: implement ReceiveResultBehaviour, odebranie zioła 接收药草
 */

public class ReceiveHerbBehaviour extends ReceiveResultBehaviour<Mage> {

    private boolean sendToMage = false;
    private AID originSender;

    public ReceiveHerbBehaviour(Mage agent, String conversationId) {
        super(agent, conversationId);
    }

    public ReceiveHerbBehaviour(Mage agent, String conversationId, AID originSender, boolean sendToMage) {
        super(agent, conversationId);
        this.sendToMage = sendToMage;
        this.originSender = originSender;
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        getMyAgent().setBusy(false);
        if(sendToMage){
            System.out.println("\n-------" + getAgent().getName() + ": I have gotten herb, now send to task-sender to handle\n");
            getAgent().addBehaviour(new SendHerbBehaviour(getMyAgent(), predicate, getConversationId(), originSender));
        }else {
            Result result = (Result) predicate;
            if (result.getValue() instanceof String) {
                ((Mage) getAgent()).getFailBuyList().add((String) result.getValue());
                System.out.println(getAgent().getName() + ": There is no one sell the herb - " + result.getValue() + " - I want to buy.");
            } else {
                SellHerb h = (SellHerb) result.getValue();
                ((Mage) getAgent()).getHerbList().add(new HerbItem(new Pair<>(h.getHerb(), h.getNum())));
                System.out.println(getAgent().getName() + ": I got and add this herb - [" + h.getHerb().getName() + " x" + h.getNum() + "] - to my herb bag from " + participant.getName() + ".\n");
            }
        }
    }
}
