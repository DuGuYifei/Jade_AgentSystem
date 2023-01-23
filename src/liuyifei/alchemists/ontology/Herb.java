package liuyifei.alchemists.ontology;

import jade.content.Concept;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:16
 * Description: koncept opisujący zioło 描述药草的概念
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Herb implements Concept {

    private String name;

}
