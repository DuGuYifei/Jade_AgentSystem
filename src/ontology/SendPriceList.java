package ontology;

import jade.content.AgentAction;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/22 15:44
 * Description: Task of send price list from sellet to student
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SendPriceList implements AgentAction {

    private PriceList priceList;

    @Override
    public boolean equals(Object sp){
        if(!(sp instanceof SendPriceList))
            return false;
        return priceList.getSellerAid().equals(((SendPriceList)sp).priceList.getSellerAid());
    }

    @Override
    public int hashCode(){
        return priceList.getSellerAid().hashCode();
    }
}
