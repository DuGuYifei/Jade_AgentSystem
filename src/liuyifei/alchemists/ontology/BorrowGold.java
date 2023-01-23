package liuyifei.alchemists.ontology;

import jade.content.AgentAction;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/3 9:29
 * Description: borrow gold action
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BorrowGold implements AgentAction {
    private Gold gold;
}
