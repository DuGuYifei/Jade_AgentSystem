package liuyifei.alchemists.ontology;

import jade.content.AgentAction;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/4 13:11
 * Description: Send Recipe to Mage
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SendRecipe implements AgentAction {
    private Recipe recipe;
}
