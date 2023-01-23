package liuyifei.alchemists.agents;

import liuyifei.alchemists.behaviours.RegisterServiceBehaviour;
import liuyifei.alchemists.behaviours.alchemist.AlchemistBehaviour;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;


/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/3 23:51
 * Description: agent sprzedający mikstury 代理卖药水
 *              1. all prices of herbs from alchemist are 20 golds (can be set in configure.properties)
 *              2. fee for producing portion is 25 golds (can be set in configure.properties)
 */

public class Alchemist extends BaseAgent {

    @Getter
    private final HashMap<String, Integer> herbs = new HashMap<>();

    @Getter
    @Setter
    private Integer money = 0;

    public Alchemist() {
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
                herbs.put(h_num[0], Integer.parseInt(h_num[1]));
                System.out.println("\t" + h_num[0] + (h_num[0].length() < 8? "\t\t\t\t":(h_num[0].length() > 12? "\t\t" : "\t\t\t")) + ResourceBundle.getBundle("liuyifei.alchemists.configure").getString("alchemist.herbPrice") + "\t\t\t" + h_num[1]);
            }
        }
        addBehaviour(new RegisterServiceBehaviour(this, "alchemist"));
        addBehaviour(new AlchemistBehaviour(this));
    }

    @Override
    public void doDelete(){
        System.out.println(getName() + ": I earn " + getMoney() + " golds today.");
        super.doDelete();
    }
}
