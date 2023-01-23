package liuyifei.alchemists.behaviours.mage;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.behaviours.RequestActionBehaviour;
import liuyifei.alchemists.ontology.SellPotion;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:15
 * Description: implement RequestActionBehaviour, żądanie mikstury 药水请求
 */

public class RequestPotionBehaviour extends RequestActionBehaviour<Mage, SellPotion> {

    public RequestPotionBehaviour(Mage agent, AID participant, SellPotion action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        getMyAgent().setBusy(true);
        getMyAgent().getGold().setGold(getMyAgent().getGold().getGold() - getAction().getMoney());
        System.out.println(getAgent().getName() + ": I want this potion - " + this.getAction().getPotion().getName() + ", dear " + getParticipant().getName() + ".");
        //getAction().setHerbList(getMyAgent().getHerbList());
        return new ReceivePotionBehaviour(this.getMyAgent(), conversationId);
    }

}
