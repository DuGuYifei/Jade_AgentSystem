package liuyifei.alchemists.behaviours.recipeLibrary;

import jade.content.onto.basic.Action;
import jade.core.AID;
import liuyifei.alchemists.agents.RecipeLibrary;
import liuyifei.alchemists.behaviours.WaitingBehaviour;
import liuyifei.alchemists.ontology.SendRecipe;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/4 15:52
 * Description: implement WaitingBehaviour
 */

public class RecipeLibraryBehaviour extends WaitingBehaviour<RecipeLibrary> {

    public RecipeLibraryBehaviour(RecipeLibrary myAgent) {
        super(myAgent);
    }

    @Override
    protected void action(Action action, String conversationId, AID participant) {
        if (action.getAction() instanceof SendRecipe) {
            myAgent.addBehaviour(new SendRecipeBehaviour(myAgent, (SendRecipe) action.getAction(), conversationId, participant));
        }
    }

}
