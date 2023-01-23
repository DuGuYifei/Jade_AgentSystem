package ontology;

import jade.content.AgentAction;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/22 23:09
 * Description: students broadcast ask price task
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BroadcastPriceTask implements AgentAction {

    private SendPriceList priceTask;

    private AgentSignature signature;

}
