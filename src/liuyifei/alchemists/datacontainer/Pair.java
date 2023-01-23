package liuyifei.alchemists.datacontainer;

import lombok.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/7 18:25
 * Description:
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Pair <X, Y>{
    private X x;
    private Y y;
}
