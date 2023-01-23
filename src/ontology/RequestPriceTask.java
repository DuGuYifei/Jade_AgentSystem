package ontology;

import jade.content.AgentAction;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/22 23:10
 * Description: student ask other students for price task
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestPriceTask implements AgentAction {

    private SendPriceList priceTask;
    private boolean approve;
    private int taskCount;

}
