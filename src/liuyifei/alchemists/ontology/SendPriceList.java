package liuyifei.alchemists.ontology;

import jade.content.AgentAction;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/5 11:34
 * Description: Send price list to mage
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SendPriceList implements AgentAction {
    private PriceList priceList;
}
