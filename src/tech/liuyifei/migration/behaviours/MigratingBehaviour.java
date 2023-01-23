package tech.liuyifei.migration.behaviours;

import jade.core.Location;
import jade.core.behaviours.TickerBehaviour;
import tech.liuyifei.migration.agents.MigratingAgent;


/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/10/30 2:41
 * Description: behavior responsible for migration between containers 负责容器间迁移的行为
 */

public class MigratingBehaviour extends TickerBehaviour {
    protected final MigratingAgent myAgent;

    public MigratingBehaviour(MigratingAgent agent) {
        super(agent, 1000);
        this.myAgent = agent;
    }

    @Override
    protected void onTick() {
        myAgent.getContainer();
        myAgent.setIdLocation(myAgent.getIdLocation() + 1);
        if(myAgent.getIdLocation() >= myAgent.getLocations().size()){
            myAgent.setIdLocation(0);
        }
        Location target = (Location)myAgent.getLocations().get(myAgent.getIdLocation());
        if(myAgent.getLocations().size() > 1) {
            System.out.println("target:" + target);
            myAgent.doMove(target);
        }
    }
}
