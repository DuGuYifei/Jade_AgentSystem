package ontology;

import jade.content.AgentAction;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/17 12:25
 * Description:
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SellProduct implements AgentAction {

    private Product product;

    @Override
    public boolean equals(Object sp){
        if(!(sp instanceof SellProduct))
            return false;
        return product.getName().equals(((SellProduct)sp).product.getName());
    }

    @Override
    public int hashCode(){
        return product.getName().hashCode();
    }

}
