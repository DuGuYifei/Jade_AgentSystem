package liuyifei.alchemists.ontology;

import jade.content.Concept;
import jade.core.AID;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/5 11:32
 * Description: Price list of each agent
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PriceList implements Concept {

    private AID aid;
    private List<PriceItem> prices;


    public void addItem(PriceItem pi){
        if(prices == null)
            prices = new ArrayList<>();
        prices.add(pi);
    }
}
