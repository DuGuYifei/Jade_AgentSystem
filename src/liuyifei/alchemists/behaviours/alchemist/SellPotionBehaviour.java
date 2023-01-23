package liuyifei.alchemists.behaviours.alchemist;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import liuyifei.alchemists.agents.Alchemist;
import liuyifei.alchemists.behaviours.ActionBehaviour;
import liuyifei.alchemists.datacontainer.Pair;
import liuyifei.alchemists.datacontainer.Tuple;
import liuyifei.alchemists.ontology.Herb;
import liuyifei.alchemists.ontology.HerbItem;
import liuyifei.alchemists.ontology.PriceItem;
import liuyifei.alchemists.ontology.SellPotion;

import javax.sound.midi.Soundbank;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:07
 * Description: implement ActionBehaviour, sprzedaż mikstury 销售药水；
 */

public class SellPotionBehaviour extends ActionBehaviour<SellPotion, Alchemist> {


    public SellPotionBehaviour(Alchemist agent, SellPotion action, String conversationId, AID participant) {
        super(agent, action, conversationId, participant);
    }

    @Override
    protected Predicate performAction() {
        if(action.getLackHerbList() != null) {
            for (PriceItem plack: action.getLackHerbList()) {
                Tuple<String> lack = plack.getItem();
                String lackHerb = lack.getX();
                int lackNum = lack.getZ();
                if (myAgent.getHerbs().containsKey(lackHerb)) {
                    if (myAgent.getHerbs().get(lackHerb) >= lackNum) {
                        myAgent.getHerbs().replace(lackHerb, myAgent.getHerbs().get(lackHerb) - lackNum);
                        System.out.println(myAgent.getName() + ": I supply this lack herb - " + lackHerb + " x" + lackNum);
                        if (myAgent.getHerbs().get(lackHerb) == 0) {
                            myAgent.getHerbs().remove(lackHerb);
                        }
                    }
                }
            }
            action.getLackHerbList().clear();
        }

        if(action.getHerbList() != null) {

            for (HerbItem hi : action.getHerbList()) {
                Pair<Herb, Integer> herb_num = hi.getItem();
                Herb h = herb_num.getX();
                System.out.println(myAgent.getName() + ": I take this herb from you - [" + h + " x" + herb_num.getY() + "].");
            }
            action.getHerbList().clear();
        }

        System.out.println(myAgent.getName() + ": I have built this potion - " + action.getPotion().getName() + ". Give it to you and I get " + action.getMoney() + " golds, dear " + getParticipant().getName() + "." );
        myAgent.setMoney(myAgent.getMoney() + action.getMoney());
        action.setMoney(0);

        return new Result(action, action);
//        if (myAgent.getPotions().contains(action.getPotion())) {
//            System.out.println("Alchemist: I have this potion - " + action.getPotion().getName() + ". Give it to you, dear " + getParticipant().getName() + "." );
//            return new Result(action, action.getPotion());
//        } else {
//            System.out.println("Alchemist: I don't have this potion - " + action.getPotion().getName() +". Sorry , dear " + getParticipant().getName() + ".");
//            return new Result(action, action.getPotion().getName());
//        }
    }

}
