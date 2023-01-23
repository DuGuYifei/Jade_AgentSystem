package liuyifei.alchemists.ontology;

import jade.content.AgentAction;
import jade.core.AID;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/3 11:41
 * Description: request herb sub task to other mage
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestHerbTask implements AgentAction {
    private SellHerb sellHerb;
    private Gold gold;
    private AID target;
}
