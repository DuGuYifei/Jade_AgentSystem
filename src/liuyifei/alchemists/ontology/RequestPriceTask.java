package liuyifei.alchemists.ontology;

import jade.content.AgentAction;
import jade.core.AID;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/2 12:16
 * Description: divide request price task to another mage
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestPriceTask implements AgentAction {
    private SendPriceList sendPriceList;
    private AID target;
}
