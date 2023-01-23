package behaviours;

import agents.BaseAgent;
import jade.content.ContentElement;
import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.logging.Level;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:00
 * Description: oczekiwanie na wynik akcji żądanej od innego agenta 等待另一个代理请求的操作的结果
 */

@Log
@Getter
public abstract class ReceiveResultBehaviour <E extends BaseAgent> extends SimpleBehaviour {

    private final E myAgent;
    private final String conversationId;
    private MessageTemplate mt;
    private boolean done = false;

    public ReceiveResultBehaviour(E agent, String conversationId) {
        super(agent);
        this.myAgent = agent;
        this.conversationId = conversationId;
    }
    @Override
    public void onStart() {
        super.onStart();
        mt = MessageTemplate.MatchConversationId(conversationId);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            try {
                ContentElement ce = myAgent.getContentManager().extractContent(msg);
                handleResult((Predicate) ce, msg.getSender());
                done = true;
            } catch (Codec.CodecException | OntologyException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean done() {
        return done;
    }

    protected abstract void handleResult(Predicate predicate, AID participant);

}
