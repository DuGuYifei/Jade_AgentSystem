package liuyifei.alchemists.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.logging.Level;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:16
 * Description: klasa definiująca ontologię 定义本体的类
 */

@Log
public class AlchemyOntology extends BeanOntology {

    public static final String NAME = "alchemy-ontology";

    @Getter
    private static final AlchemyOntology instance = new AlchemyOntology(NAME);

    public AlchemyOntology(String name) {
        super(name);
        try {
            add(Herb.class);
            add(Potion.class);
            add(SellHerb.class);
            add(SellPotion.class);
            add(Recipe.class);
            add(SendRecipe.class);
            add(PriceItem.class);
            add(PriceList.class);
            add(SendPriceList.class);
            add(Gold.class);
            add(InformGold.class);
            add(BorrowGold.class);
            add(RequestPriceTask.class);
            add(SendBusyState.class);
            add(RequestHerbTask.class);
            add(BusyState.class);
        } catch (BeanOntologyException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

}
