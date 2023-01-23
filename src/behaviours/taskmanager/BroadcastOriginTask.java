package behaviours.taskmanager;

import agents.TaskManager;
import behaviours.FindServiceBehaviour;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import ontology.BroadcastTaskAction;
import ontology.ProductOntology;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 13:54
 * Description: broadcast task to all students
 */

public class BroadcastOriginTask extends FindServiceBehaviour {

    private final TaskManager taskManager;

    public BroadcastOriginTask(TaskManager agent) {
        super(agent, "student");
        this.taskManager = agent;
    }

    @Override
    protected void onResult(DFAgentDescription[] services) {
        System.out.println(myAgent.getName() + ": Broadcasting Task\n\n---------------------------\n");
        if (services != null && services.length > 0) {
            for (DFAgentDescription dfag : services) {
                AID student = dfag.getName();
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setLanguage(new SLCodec().getName());
                msg.setOntology(ProductOntology.getInstance().getName());
                msg.addReceiver(student);
                try {
                    BroadcastTaskAction bt = new BroadcastTaskAction(taskManager.getTasks());
                    Action action = new Action(myAgent.getAID(), bt);
                    myAgent.getContentManager().fillContent(msg, action);
                    myAgent.send(msg);
                } catch (Codec.CodecException | OntologyException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
