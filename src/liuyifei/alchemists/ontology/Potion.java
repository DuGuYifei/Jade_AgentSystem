package liuyifei.alchemists.ontology;

import jade.content.Concept;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:17
 * Description: koncept opisujący miksturę 描述药水的概念
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Potion implements Concept {

    private String name;

}
