package liuyifei.alchemists.behaviours;

import jade.content.AgentAction;
import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import liuyifei.alchemists.agents.BaseAgent;
import lombok.Getter;
import lombok.extern.java.Log;
import liuyifei.alchemists.ontology.AlchemyOntology;

import java.util.logging.Level;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/3 23:56
 * Description: wykonanie akcji żądanej przez innego agenta i odesłanie wyniku 执行另一个机构请求的操作并返回结果
 */

@Getter
@Log
public abstract class ActionBehaviour <T extends AgentAction, E extends BaseAgent> extends OneShotBehaviour {

    protected final E myAgent;
    protected T action;
    private final String conversationId;
    private final AID participant;

    public ActionBehaviour(E agent, T action, String conversationId, AID participant) {
        super(agent);
        this.myAgent = agent;
        this.action = action;
        this.conversationId = conversationId;
        this.participant = participant;
    }

    public ActionBehaviour(E agent, String conversationId, AID participant) {
        super(agent);
        this.myAgent = agent;
        this.conversationId = conversationId;
        this.participant = participant;
    }

    @Override
    public void action() {
        Predicate result = performAction();
        ACLMessage msg;
        if (!(((Result) result).getValue() instanceof String)) {
            msg = new ACLMessage(ACLMessage.INFORM);
        } else {
            msg = new ACLMessage(ACLMessage.REFUSE);
        }
        msg.setLanguage(new SLCodec().getName());
        msg.setOntology(AlchemyOntology.getInstance().getName());
        msg.setConversationId(conversationId);
        msg.addReceiver(participant);
        try {
            myAgent.getContentManager().fillContent(msg, result);
            myAgent.send(msg);
        } catch (Codec.CodecException | OntologyException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    protected abstract Predicate performAction();

}
