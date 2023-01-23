package behaviours.student;

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
import ontology.BroadcastPriceListAction;
import ontology.ProductOntology;
import ontology.SendPriceList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 13:53
 * Description: Broadcast price list after get it from seller
 */

public class BroadcastPriceList extends FindServiceBehaviour {

    private final BroadcastPriceListAction broadcastPriceList;

    public BroadcastPriceList(Agent agent, BroadcastPriceListAction broadcastPriceList) {
        super(agent, "student");
        this.broadcastPriceList = broadcastPriceList;
    }

    @Override
    protected void onResult(DFAgentDescription[] services) {
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
                    Action action = new Action(myAgent.getAID(), broadcastPriceList);
                    myAgent.getContentManager().fillContent(msg, action);
                    myAgent.send(msg);
                } catch (Codec.CodecException | OntologyException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
