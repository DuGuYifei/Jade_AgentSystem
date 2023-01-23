package agents;

import behaviours.RegisterServiceBehaviour;
import behaviours.Seller.SellerBehaviour;
import lombok.Getter;
import lombok.Setter;
import ontology.PriceItem;
import ontology.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/17 11:45
 * Description: seller agent
 */

public class Seller extends BaseAgent{

    @Getter
    private final List<PriceItem> sampleProduct = new ArrayList<>();

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new RegisterServiceBehaviour(this, "seller"));
        String[] args = ((String) getArguments()[0]).split("\\|");
        for(String arg:args){
            String[] productArg = arg.split(",");
            sampleProduct.add(new PriceItem(new Product(productArg[0]), Integer.parseInt(productArg[1]), getAID()));
        }
        addBehaviour(new SellerBehaviour(this));
    }

    @Override
    public void doDelete(){
        super.doDelete();
    }
}
