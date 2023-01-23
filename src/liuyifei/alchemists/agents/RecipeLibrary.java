package liuyifei.alchemists.agents;

import liuyifei.alchemists.behaviours.recipeLibrary.RecipeLibraryBehaviour;
import liuyifei.alchemists.datacontainer.Pair;
import liuyifei.alchemists.ontology.Recipe;
import liuyifei.alchemists.ontology.RecipeItem;
import lombok.Getter;
import liuyifei.alchemists.behaviours.RegisterServiceBehaviour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/3 17:19
 * Description: Search recipe of portion
 */

public class RecipeLibrary extends BaseAgent{
    @Getter
    private final List<Recipe> recipes = new ArrayList<>();

    public RecipeLibrary() {
    }

    @Override
    protected void setup() {
        super.setup();

        for(Object o: getArguments()){
            System.out.println(this.getName() + ": I have these recipes from arguments:"+ o);
            List<String> splits = Arrays.asList(((String) o).split("\\|"));
            recipes.add(new Recipe(splits.get(0), splitParam(splits.get(1))));
        }

        addBehaviour(new RegisterServiceBehaviour(this, "recipeLibrary"));
        addBehaviour(new RecipeLibraryBehaviour(this));
    }

    private List<RecipeItem> splitParam(String param){
        List<RecipeItem> res = new ArrayList<>();

        String[] strings = param.split(",");
        for(String s : strings){
            String[] nameQuantity = s.split("-");
            res.add(new RecipeItem(new Pair<>(nameQuantity[0], Integer.parseInt(nameQuantity[1]))));
        }

        return res;
    }
}
