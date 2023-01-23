package liuyifei.alchemists.ontology;

import jade.content.AgentAction;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:17
 * Description: akcja sprzedaży zioła 药草销售行为
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SellHerb implements AgentAction {

    private Herb herb;

    private Integer money;

    private Integer num;

}

