package ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.logging.Level;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/17 11:35
 * Description: ontology of product
 */

@Log
public class ProductOntology extends BeanOntology {

    public static final String NAME = "product-ontology";

    @Getter
    private static final ProductOntology instance = new ProductOntology(NAME);

    public ProductOntology(String name) {
        super(name);
        try {
            add(Product.class);
            add(PriceItem.class);
            add(PriceList.class);
            add(SellProduct.class);
            add(SendPriceList.class);
            add(BroadcastTaskAction.class);
            add(BroadcastShopTask.class);
            add(BroadcastPriceTask.class);
            add(BroadcastPriceListAction.class);
            add(BroadcastProductAction.class);
            add(RequestShopTask.class);
            add(RequestPriceTask.class);
            add(AgentSignature.class);
        } catch (BeanOntologyException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

}
