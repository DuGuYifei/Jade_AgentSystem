package ontology;

import jade.content.Concept;
import jade.core.AID;
import jade.util.leap.HashMap;
import lombok.*;

import java.util.List;


/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/17 11:44
 * Description: Price item list
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PriceList implements Concept {

    private AID sellerAid;

    private List<PriceItem> priceItems;

}
