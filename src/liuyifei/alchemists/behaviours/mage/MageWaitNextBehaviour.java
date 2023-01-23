package liuyifei.alchemists.behaviours.mage;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import liuyifei.alchemists.agents.Mage;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/5 11:25
 * Description: wait get recipes
 */

public class MageWaitNextBehaviour extends SimpleBehaviour {
    private final Behaviour behaviour;
    private final Mage myAgent;
    private final String nextFunction;
    private final String nextStep;

    public MageWaitNextBehaviour(Behaviour behaviour, Mage mage, String nextFunction, String nextStep) {
        this.behaviour = behaviour;
        this.myAgent = mage;
        this.nextFunction = nextFunction;
        this.nextStep = nextStep;
    }

    @SneakyThrows
    @Override
    public void action() {
        if(done()) {
            System.out.println(nextStep);
            Class<?> cls = Class.forName("liuyifei.alchemists.agents.Mage");
            Method m = cls.getMethod(nextFunction);
            m.invoke(myAgent);
        }
    }

    @Override
    public boolean done() {
        return behaviour.done();
    }
}