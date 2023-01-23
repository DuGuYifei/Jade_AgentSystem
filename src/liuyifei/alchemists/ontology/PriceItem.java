package liuyifei.alchemists.ontology;

import jade.content.Concept;
import liuyifei.alchemists.datacontainer.Tuple;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/6 11:59
 * Description: Used for price list
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PriceItem implements Concept {
    private Tuple<String> item;
}
