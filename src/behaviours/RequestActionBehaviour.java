package behaviours;

import agents.BaseAgent;
import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.extern.java.Log;
import ontology.ProductOntology;

import java.util.UUID;
import java.util.logging.Level;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:01
 * Description: żądanie od innego agenta wykonania określonej akcji 请求另一个代理执行特定操作
 */

@Getter
@Log
public abstract class RequestActionBehaviour <E extends BaseAgent, T extends AgentAction> extends OneShotBehaviour {

    private final E myAgent;
    private final AID participant;
    private final T action;
    private String conversationId = null;

    public RequestActionBehaviour(E agent, AID participant, T action) {
        this.participant = participant;
        this.action = action;
        this.myAgent = agent;
    }

    public RequestActionBehaviour(E agent, AID participant, T action, String conversationId) {
        this.participant = participant;
        this.action = action;
        this.myAgent = agent;
        this.conversationId = conversationId;
    }

    @Override
    public void action() {
        if(conversationId == null)
            conversationId = UUID.randomUUID().toString();

        Action action = new Action(participant, this.action);

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(new SLCodec().getName());
        request.setOntology(ProductOntology.getInstance().getName());
        request.addReceiver(participant);
        request.setSender(myAgent.getAID());
        request.setConversationId(conversationId);

        try {
            behaviours.ReceiveResultBehaviour resultBehaviour = createResultBehaviour(conversationId);
            myAgent.getContentManager().fillContent(request, action);
            myAgent.getActiveConversationIds().add(conversationId);
            myAgent.send(request);

            if (getParent() != null && getParent() instanceof SequentialBehaviour) {
                ((SequentialBehaviour)getParent()).addSubBehaviour(resultBehaviour);
            } else {
                myAgent.addBehaviour(resultBehaviour);
            }
        } catch (Codec.CodecException | OntologyException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    protected abstract behaviours.ReceiveResultBehaviour createResultBehaviour(String conversationId);
}
