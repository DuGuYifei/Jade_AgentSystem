package ontology;

import jade.content.AgentAction;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/22 23:09
 * Description: student ask other students for shop task
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestShopTask implements AgentAction {

    private SellProduct shopTask;
    private boolean approve;
    private int taskCount;

}
