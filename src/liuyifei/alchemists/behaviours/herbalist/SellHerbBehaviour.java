package liuyifei.alchemists.behaviours.herbalist;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import liuyifei.alchemists.agents.Herbalist;
import liuyifei.alchemists.behaviours.ActionBehaviour;
import liuyifei.alchemists.ontology.SellHerb;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:10
 * Description: implement ActionBehaviour, sprzedaż zioła 销售药草
 */

public class SellHerbBehaviour extends ActionBehaviour<SellHerb, Herbalist> {

    public SellHerbBehaviour(Herbalist agent, SellHerb action, String conversationId, AID participant) {
        super(agent, action, conversationId, participant);
    }

    @Override
    protected Predicate performAction() {
        if (myAgent.getHerbs().containsKey(action.getHerb())) {
            System.out.println(myAgent.getName() + ": I have this herb - " + action.getHerb().getName() +". Give it to you and I get " + action.getMoney() + " golds, dear " + getParticipant().getName() + ".");
            myAgent.setMoney(myAgent.getMoney() + action.getMoney());
            action.setMoney(0);
            return new Result(action, action);
        } else {
            System.out.println(myAgent.getName() + ": I don't have this herb - " + action.getHerb().getName() +". Sorry , dear " + getParticipant().getName() + ".");
            return new Result(action, action.getHerb().getName());
        }
    }

}
