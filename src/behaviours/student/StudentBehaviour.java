package behaviours.student;

import agents.Student;
import jade.content.AgentAction;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import ontology.*;

import java.util.Map;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/22 21:45
 * Description: Students behaviours - check tasks list
 */

public class StudentBehaviour extends CyclicBehaviour {

    protected final Student myAgent;

    public StudentBehaviour(Student myAgent) {
        super(myAgent);
        this.myAgent = myAgent;
    }


    @Override
    public void action() {

        if(myAgent.isBusy() || myAgent.isAskingTask()){
            //System.out.println(myAgent.getName() + " " + myAgent.isBusy() + myAgent.isAskingTask());
            //System.out.println(myAgent.getName() + " " + myAgent.getWantDo());
            return;
        }
        // System.out.println("TEST" + myAgent.getShopTaskMap());

        // firstly request price task
        for (Map.Entry<SendPriceList, AgentSignature> entry : myAgent.getPriceTaskMap().entrySet()){
            if(CanTakeTask(entry, new RequestPriceTask(entry.getKey(), true, myAgent.getTaskCount()))) {
                // System.out.println(myAgent.getPriceTaskMap().entrySet());

                myAgent.setWantDo(entry.getKey());
                return;
            }
        }

        // secondly shopping task
        // start after finish request price tasks
        if(myAgent.getPriceTaskMap().size() > 0){
            return;
        }
        for (Map.Entry<SellProduct, AgentSignature> entry : myAgent.getShopTaskMap().entrySet()){
            if(CanTakeTask(entry, new RequestShopTask(entry.getKey(), true, myAgent.getTaskCount()))) {
                myAgent.setWantDo(entry.getKey());
                return;
            }
        }
    }

    private <E extends AgentAction, T extends AgentAction> boolean CanTakeTask(Map.Entry<E, AgentSignature> entry, T requestAction){
        if(entry.getValue() != null){
            // if over 10 seconds
            if(System.currentTimeMillis() - entry.getValue().getDatetime().getTime() > 10000){
                DFAgentDescription[] studentServices = findStudents();
                // -1: self not response self
                myAgent.setRequestTaskCount(studentServices.length - 1);
                myAgent.setRequestTaskApprove(true);
                myAgent.setAskingTask(true);

                for(DFAgentDescription student:studentServices){
                    if(student.getName().equals(myAgent.getAID()))
                        continue;
                    myAgent.addBehaviour(new RequestTask(myAgent, student.getName(), requestAction));
                }
                return true;
            }
        }else{
            DFAgentDescription[] studentServices = findStudents();
            // -1: self not response self
            myAgent.setRequestTaskCount(studentServices.length - 1);
            myAgent.setRequestTaskApprove(true);
            myAgent.setAskingTask(true);
            for(DFAgentDescription student:studentServices){
                if(student.getName().equals(myAgent.getAID()))
                    continue;
                myAgent.addBehaviour(new RequestTask(myAgent, student.getName(), requestAction));
            }
            return true;
        }
        return false;
    }

    private DFAgentDescription[] findStudents(){
        DFAgentDescription dfad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        DFAgentDescription[] studentServices = new DFAgentDescription[0];
        try {
            sd.setType("student");
            dfad.addServices(sd);
            studentServices = DFService.search(myAgent, dfad);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        return studentServices;
    }
}
