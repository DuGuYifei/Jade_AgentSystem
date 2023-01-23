package liuyifei.alchemists.behaviours.mage;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import liuyifei.alchemists.agents.Mage;

import java.util.SortedMap;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/5 13:42
 * Description: wait prev stop
 */

public class MageWaitBehaviour extends SimpleBehaviour {
    private final Behaviour behaviour;
    private final String nextStep;
    private Mage mage;
    private boolean askGold = false;

    public MageWaitBehaviour(Behaviour behaviour, String nextStep) {
        this.behaviour = behaviour;
        this.nextStep = nextStep;
    }


    public MageWaitBehaviour(Behaviour behaviour, String nextStep, Mage mage, boolean askGold) {
        this.behaviour = behaviour;
        this.nextStep = nextStep;
        this.askGold = askGold;
        this.mage = mage;
    }


    @Override
    public void action() {
        if(done()) {
            System.out.println(nextStep);
            if(askGold){
                System.out.println("I remain golds " + mage.getGold().getGold() + "\n------------\n");
            }
        }
    }


    @Override
    public boolean done() {
        return behaviour.done();
    }
}
