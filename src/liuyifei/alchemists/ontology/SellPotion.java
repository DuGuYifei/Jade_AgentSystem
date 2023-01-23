package liuyifei.alchemists.ontology;

import jade.content.AgentAction;
import liuyifei.alchemists.datacontainer.Pair;
import liuyifei.alchemists.datacontainer.Tuple;
import lombok.*;

import java.util.List;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:17
 * Description: akcja sprzedaży mikstury 药水销售行为
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SellPotion implements AgentAction {

    private Potion potion;
    private List<HerbItem> herbList;
    private List<PriceItem> lackHerbList;

    private Integer money;
}
