package liuyifei.alchemists.behaviours.alchemist;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import liuyifei.alchemists.datacontainer.Tuple;
import liuyifei.alchemists.agents.Alchemist;
import liuyifei.alchemists.behaviours.ActionBehaviour;
import liuyifei.alchemists.ontology.PriceItem;
import liuyifei.alchemists.ontology.SendPriceList;

import java.util.ResourceBundle;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/5 12:22
 * Description: send price list
 */

public class SendPriceListBehaviour extends ActionBehaviour<SendPriceList, Alchemist> {

    public SendPriceListBehaviour(Alchemist agent, SendPriceList action, String conversationId, AID participant) {
        super(agent, action, conversationId, participant);
    }

    @Override
    protected Predicate performAction() {
        if (myAgent.getHerbs().size() > 0) {
            System.out.println(myAgent.getName() + ": This is my price list of herbs in my backup, dear " + getParticipant().getName() + ".");
            for(String h: myAgent.getHerbs().keySet()) {
                if(myAgent.getHerbs().get(h) > 0) {
                    action.getPriceList().addItem(new PriceItem(new Tuple<>(h, Integer.parseInt(ResourceBundle.getBundle("liuyifei.alchemists.configure").getString("alchemist.herbPrice")), myAgent.getHerbs().get(h))));
                    System.out.println("\t" + h + (h.length() < 8? "\t\t\t\t":(h.length() > 12? "\t\t" : "\t\t\t")) + ResourceBundle.getBundle("liuyifei.alchemists.configure").getString("alchemist.herbPrice") + " golds");
                }
            }
            return new Result(action, action.getPriceList());
        } else {
            System.out.println(myAgent.getName() + ": I don't have any herb in my backup. Sorry, dear " + getParticipant().getName() + ".");
            return new Result(action, myAgent.getName());
        }
    }

}
