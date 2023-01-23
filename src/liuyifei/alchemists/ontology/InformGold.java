package liuyifei.alchemists.ontology;

import jade.content.AgentAction;
import jade.core.AID;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/3 4:34
 * Description: inform gold to other mages
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class InformGold implements AgentAction {
    private Gold gold;
    private AID owner;
}
