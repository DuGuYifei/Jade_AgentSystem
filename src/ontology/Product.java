package ontology;

import jade.content.Concept;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/17 12:25
 * Description:
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product implements Concept {

    private String name;

}
