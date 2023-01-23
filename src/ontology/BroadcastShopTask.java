package ontology;

import jade.content.AgentAction;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/22 23:08
 * Description: students broadcast go shopping task
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BroadcastShopTask implements AgentAction {

    private SellProduct shopTask;

    private AgentSignature signature;

}
