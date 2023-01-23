package liuyifei.alchemists.ontology;

import jade.content.AgentAction;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/2 13:08
 * Description: send busy state to task divider
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SendBusyState implements AgentAction {
    private BusyState busyState;
    private String conversationId;
}
