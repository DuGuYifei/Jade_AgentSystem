package liuyifei.alchemists.datacontainer;

import jade.core.AID;
import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/5 12:01
 * Description: data type
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Tuple<X> implements Comparable<Tuple<X>>{
    private X x;
    private Integer y;
    private Integer z;

    @Override
    public int compareTo(Tuple<X> o) {
        return this.getY().equals(o.getY()) ? o.getZ() - this.getZ() : this.getY() - o.getY();
    }
}