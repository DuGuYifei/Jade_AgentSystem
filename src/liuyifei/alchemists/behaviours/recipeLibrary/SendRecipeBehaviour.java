package liuyifei.alchemists.behaviours.recipeLibrary;

import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import liuyifei.alchemists.agents.RecipeLibrary;
import liuyifei.alchemists.behaviours.ActionBehaviour;
import liuyifei.alchemists.ontology.Recipe;
import liuyifei.alchemists.ontology.SendRecipe;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/4 15:53
 * Description: send recipe to mage
 */

public class SendRecipeBehaviour extends ActionBehaviour<SendRecipe, RecipeLibrary> {

    public SendRecipeBehaviour(RecipeLibrary agent, SendRecipe action, String conversationId, AID participant) {
        super(agent, action, conversationId, participant);
    }

    @Override
    protected Predicate performAction() {
        System.out.println("RecipeLibrary: In searching...:");

        for(Recipe recipe : myAgent.getRecipes()){
            if(recipe.getPotionName().equals(action.getRecipe().getPotionName())){
                System.out.println("RecipeLibrary: I find the recipe: [" + action.getRecipe().getPotionName() +"] -- " + recipe.getHerbNameList());
                action.setRecipe(recipe);
                return new Result(action, action.getRecipe());
            }
        }
        System.out.println("RecipeLibrary: No recipe found for [" + action.getRecipe().getPotionName() + "]");
        return new Result(action, action.getRecipe().getPotionName());
    }
}
