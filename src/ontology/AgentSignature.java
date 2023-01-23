package ontology;

import jade.content.Concept;
import lombok.*;

import java.util.Date;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/22 15:48
 * Description: The signature of agent
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AgentSignature implements Concept {

    private String name;
    private Date datetime;

}
