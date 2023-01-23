package liuyifei.alchemists.agents;

import jade.core.Agent;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/12/4 15:04
 * Description: Quick setup agents by configure.properties
 */

public class EnvStart extends Agent {

    public final List agentList = new ArrayList(); // AgentController

    @Override
    protected void setup(){
        String[] alchemistStr = ((String)ResourceBundle.getBundle("liuyifei.alchemists.configure").getObject("alchemist")).split("\\$", -1);
        int n = alchemistStr.length;
        for(int i = 0; i < n; i++){
            String nickname = "alchemist" + i;
            String classname = "liuyifei.alchemists.agents.Alchemist";
            String[] args = new String[]{alchemistStr[i]};
            try {
                AgentController ac = this.getContainerController().createNewAgent(nickname, classname, args);
                ac.start();
                agentList.add(ac);
                Thread.sleep(200);
            } catch (StaleProxyException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        String[] herbalistStr = ((String)ResourceBundle.getBundle("liuyifei.alchemists.configure").getObject("herbalist")).split("\\$", -1);
        n = herbalistStr.length;
        for(int i = 0; i < n; i++){
            String nickname = "herbalist" + i;
            String classname = "liuyifei.alchemists.agents.Herbalist";
            String[] args = new String[]{herbalistStr[i]};
            try {
                AgentController ac = this.getContainerController().createNewAgent(nickname, classname, args);
                ac.start();
                agentList.add(ac);
                Thread.sleep(200);
            } catch (StaleProxyException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            String[] libraryArgs = ((String)ResourceBundle.getBundle("liuyifei.alchemists.configure").getObject("recipe")).split("\\$");
            String nickname = "library";
            String classname = "liuyifei.alchemists.agents.RecipeLibrary";
            AgentController ac = this.getContainerController().createNewAgent(nickname, classname, libraryArgs);
            ac.start();
            agentList.add(ac);
            Thread.sleep(200);
        } catch (StaleProxyException | InterruptedException e) {
            e.printStackTrace();
        }

        String[] mageStr = ((String)ResourceBundle.getBundle("liuyifei.alchemists.configure").getObject("mage")).split("\\$");
        n = mageStr.length;
        for(int i = 0; i < n; i++){
            String nickname = "mage" + i;
            String classname = "liuyifei.alchemists.agents.Mage";
            String[] args = new String[]{mageStr[i]};
            try {
                AgentController ac = this.getContainerController().createNewAgent(nickname, classname, args);
                ac.start();
                agentList.add(ac);
                Thread.sleep(200);
            } catch (StaleProxyException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void doDelete(){
        int n = agentList.size();
        for (int i = 0; i < n; i++) {
            AgentController ac = (AgentController) agentList.get(i);
            try {
                ac.kill();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
        super.doDelete();
    }

}
