package liuyifei.alchemists.ontology;

import jade.content.Concept;
import liuyifei.alchemists.datacontainer.Pair;
import lombok.*;

import java.util.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/7 13:32
 * Description: The recipe of potion which shows herbs the potion needs.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Recipe implements Concept {
    private String potionName;

    private List<RecipeItem> herbNameList;
}

//public class Recipe {
//    @Getter
//    private final Dictionary<String, List<String>> recipe = new Hashtable<>();
//
//    @Getter
//    private static final Recipe recipeInstance = new Recipe();
//
//    public Recipe(){
//        recipe.put("Shrouding Potion", new ArrayList<>(Arrays.asList("Ragveil","Netherbloom")));
//        recipe.put("Heroic Potion", new ArrayList<>(Arrays.asList("Terocone","Ancient Lichen")));
//        recipe.put("Ghost Dye", new ArrayList<>(Arrays.asList("Purple Dye", "Ghost Mushroom")));
//    }
//
//}
