package liuyifei.alchemists.agents;

import lombok.Getter;
import liuyifei.alchemists.behaviours.RegisterServiceBehaviour;
import liuyifei.alchemists.behaviours.herbalist.HerbalistBehaviour;
import liuyifei.alchemists.ontology.Herb;
import lombok.Setter;

import java.util.HashMap;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/3 23:55
 * Description: agent sprzedający zioła 代理卖药草
 */

public class Herbalist extends BaseAgent{

    @Getter
    private final HashMap<Herb, Integer> herbs = new HashMap<>();

    @Getter
    @Setter
    private Integer money = 0;

    public Herbalist() {
    }

    @Override
    protected void setup() {
        super.setup();
        String[] herbArgs = ((String)getArguments()[0]).split(",");
        System.out.println(this.getName());
        if(!herbArgs[0].equals("")) {
            System.out.println("\t: I can have these herbs from arguments:");
            System.out.println("\tHerb\t\t\t\tPrice\t\tQuantity");
            for (String h : herbArgs) {
                String[] h_num = h.split("-");
                herbs.put(new Herb(h_num[0]), Integer.parseInt(h_num[1]));
                System.out.println("\t" + h_num[0] + (h_num[0].length() < 8? "\t\t\t\t":(h_num[0].length() > 12? "\t\t" : "\t\t\t")) + h_num[1] + "\t\t\t∞");
            }
        }
        addBehaviour(new RegisterServiceBehaviour(this, "herbalist"));
        addBehaviour(new HerbalistBehaviour(this));
    }

    @Override
    public void doDelete(){
        System.out.println(getName() + ": I earn " + getMoney() + " golds today.");
        super.doDelete();
    }
}
