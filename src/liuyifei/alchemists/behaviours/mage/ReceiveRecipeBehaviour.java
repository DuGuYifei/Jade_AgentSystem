package liuyifei.alchemists.behaviours.mage;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import liuyifei.alchemists.agents.Mage;
import liuyifei.alchemists.behaviours.ReceiveResultBehaviour;
import liuyifei.alchemists.ontology.Recipe;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/4 20:57
 * Description: receive recipe from library
 */

public class ReceiveRecipeBehaviour  extends ReceiveResultBehaviour<Mage> {

    public ReceiveRecipeBehaviour(Mage agent, String conversationId) {
        super(agent, conversationId);
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        //getMyAgent().setBusy(false);
        Result result = (Result) predicate;
        if (result.getValue() instanceof String) {
            ((Mage) getAgent()).getNoRecipeList().add((String) result.getValue());
            System.out.println(getAgent().getName() + ": There is no such potion recorded in library - [" + result.getValue() + "].\n");
        } else {
            Recipe p = (Recipe) result.getValue();
            ((Mage) getAgent()).getRecipeList().add(p);
            System.out.println(getAgent().getName() + ": I got and add this recipe - [" + p.getPotionName() + "] - to shopping plan.\n");
        }
    }
}
