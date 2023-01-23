package ontology;

import jade.content.Concept;
import jade.core.AID;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 9:08
 * Description: the item of price
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PriceItem implements Concept, Comparable<PriceItem>{

    private Product product;
    private Integer price;
    private AID sellerAID;

    @Override
    public int compareTo(PriceItem o) {
        return price - o.price;
    }
}