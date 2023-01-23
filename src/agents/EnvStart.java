package agents;

import jade.core.Agent;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ResourceBundle;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 8:40
 * Description: start environment with some students and one task manager
 */

public class EnvStart extends Agent {

    public final List agentList = new ArrayList(); // AgentController

    @Override
    protected void setup() {
        super.setup();
//        String[] studentStr = ((String)ResourceBundle.getBundle("configure").getObject("student")).split("\\$");
//        int n = studentStr.length;
//        for(int i = 0; i < n; i++){
//            String nickname = "student" + i;
//            String classname = "agents.Student";
//            String[] args = new String[]{studentStr[i]};
//            try {
//                AgentController ac = this.getContainerController().createNewAgent(nickname, classname, args);
//                ac.start();
//                agentList.add(ac);
//            } catch (StaleProxyException e) {
//                e.printStackTrace();
//            }
//        }

        int n = Integer.parseInt(((String)ResourceBundle.getBundle("configure").getObject("student")));
        for(int i = 0; i < n; i++){
            String nickname = "student" + i;
            String classname = "agents.Student";
            String[] args = new String[0];
            try {
                AgentController ac = this.getContainerController().createNewAgent(nickname, classname, args);
                ac.start();
                agentList.add(ac);
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        String[] sellerStr = ((String)ResourceBundle.getBundle("configure").getObject("seller")).split("\\$");
        n = sellerStr.length;
        for(int i = 0; i < n; i++){
            String nickname = "seller" + i;
            String classname = "agents.Seller";
            String[] args = new String[]{sellerStr[i]};
            try {
                AgentController ac = this.getContainerController().createNewAgent(nickname, classname, args);
                ac.start();
                agentList.add(ac);
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String task = ((String)ResourceBundle.getBundle("configure").getObject("task"));
        String nickname = "taskManager0";
        String classname = "agents.TaskManager";
        String[] args = new String[]{task};
        try {
            AgentController ac = this.getContainerController().createNewAgent(nickname, classname, args);
            ac.start();
            agentList.add(ac);
        } catch (StaleProxyException e) {
            e.printStackTrace();
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
