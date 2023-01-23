package ontology;

import jade.content.AgentAction;
import lombok.*;

import java.util.List;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/22 18:06
 * Description: task manager broadcast tasks
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BroadcastTaskAction implements AgentAction {
    private List<String> productNames;
}
