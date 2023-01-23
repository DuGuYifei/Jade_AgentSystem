package behaviours.student;

import agents.Student;
import behaviours.FindServiceBehaviour;
import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import ontology.ProductOntology;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 13:48
 * Description: Broadcast I will take this task after approving by all others
 */

public class BroadcastTask<E extends AgentAction> extends FindServiceBehaviour {

    private final E broadcastTask;
    private final Student student;

    public BroadcastTask(Student agent, E broadcastTask) {
        super(agent, "student");
        this.broadcastTask = broadcastTask;
        this.student = agent;
    }

    @Override
    protected void onResult(DFAgentDescription[] services) {
        student.setTaskCount(student.getTaskCount() + 1);
        if (services != null && services.length > 0) {
            for (DFAgentDescription dfag : services) {
                AID student = dfag.getName();
                if (student.equals(myAgent.getAID())) {
                    continue;
                }
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setLanguage(new SLCodec().getName());
                msg.setOntology(ProductOntology.getInstance().getName());
                msg.addReceiver(student);
                try {
                    Action action = new Action(myAgent.getAID(), broadcastTask);
                    myAgent.getContentManager().fillContent(msg, action);
                    myAgent.send(msg);
                } catch (Codec.CodecException | OntologyException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
