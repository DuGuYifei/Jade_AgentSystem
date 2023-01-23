package liuyifei.alchemists.ontology;

import jade.content.Concept;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/3 5:26
 * Description: busy state of mage
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BusyState implements Concept {
    private boolean isBusy;
}
