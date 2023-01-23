package liuyifei.alchemists.ontology;

import jade.content.Concept;
import liuyifei.alchemists.datacontainer.Pair;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/7 22:09
 * Description: FOr sellpotion.herblist
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class HerbItem implements Concept {

    private Pair<Herb, Integer> item;
}
