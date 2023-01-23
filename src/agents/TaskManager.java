package agents;

import behaviours.FindServiceBehaviour;
import behaviours.RegisterServiceBehaviour;
import behaviours.student.BroadcastTask;
import behaviours.taskmanager.BroadcastOriginTask;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import ontology.BroadcastTaskAction;
import ontology.ProductOntology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/17 11:58
 * Description: the manager who bring task to students
 */

public class TaskManager extends BaseAgent {
    @Getter
    private final List<String> tasks = new ArrayList<>();

    @Override
    protected void setup(){
        super.setup();
        addBehaviour(new RegisterServiceBehaviour(this, "task-manager"));
        String[] args = ((String) getArguments()[0]).split("\\|");
        tasks.addAll(Arrays.asList(args));
        //BroadcastTask();
        addBehaviour(new BroadcastOriginTask(this));
    }

//    private void BroadcastTask(){
//        addBehaviour(
//                new FindServiceBehaviour(this, "student") {
//                    @Override
//                    protected void onResult(DFAgentDescription[] services) {
//                        System.out.println(getName() + ": Broadcasting Task\n\n---------------------------\n");
//                        if (services != null && services.length > 0) {
//                            for (DFAgentDescription dfag : services) {
//                                AID student = dfag.getName();
//                                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//                                msg.setLanguage(new SLCodec().getName());
//                                msg.setOntology(ProductOntology.getInstance().getName());
//                                msg.addReceiver(student);
//                                try {
//                                    BroadcastTaskAction bt = new BroadcastTaskAction(tasks);
//                                    Action action = new Action(getAID(), bt);
//                                    myAgent.getContentManager().fillContent(msg, action);
//                                    send(msg);
//                                } catch (Codec.CodecException | OntologyException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                });
//    }
}
