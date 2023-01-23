package liuyifei.alchemists.behaviours.mage;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import liuyifei.alchemists.ontology.*;
import lombok.extern.java.Log;
import liuyifei.alchemists.agents.Mage;


/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/4 0:11
 * Description: oczekiwanie na zakończenie innego zachowania 等待另一个行为的结束
 */

@Log
public class MageBehaviour extends CyclicBehaviour {

    protected final Mage myAgent;

    public MageBehaviour(Mage myAgent) {
        super(myAgent);
        this.myAgent = myAgent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchAll();
        for (String id : myAgent.getActiveConversationIds()) {
            mt = MessageTemplate.and(mt, MessageTemplate.not(MessageTemplate.MatchConversationId(id)));
        }
        ACLMessage message = myAgent.receive(mt);
        if (message != null) {
            try {
                ContentElement ce = null;
                try {
                    ce = myAgent.getContentManager().extractContent(message);
                } catch (Codec.CodecException | OntologyException e) {
                    e.printStackTrace();
                }
                //System.out.println(conversationId);
                if (ce instanceof Action) {
                    Action action = (Action) ce;
                    String conversationId = message.getConversationId();
                    AID participant = message.getSender();
                    if (action.getAction() instanceof RequestPriceTask) {
                        //System.out.println("====price task " + conversationId);
                        System.out.println(getAgent().getName() + ": I got the task to request price for " + ((RequestPriceTask) action.getAction()).getTarget().getName());
                        getAgent().addBehaviour(new RequestPriceBehaviour((Mage) getAgent(), ((RequestPriceTask) action.getAction()).getTarget(), ((RequestPriceTask) action.getAction()).getSendPriceList(), conversationId, participant, true));
                    }else if(action.getAction() instanceof RequestHerbTask){
                        System.out.println(getAgent().getName() + ": I got the task to request herb for " + ((RequestHerbTask) action.getAction()).getTarget().getName());
                        myAgent.getGold().setGold(myAgent.getGold().getGold() + ((RequestHerbTask) action.getAction()).getGold().getGold());
                        getAgent().addBehaviour(new RequestHerbBehaviour((Mage) getAgent(), ((RequestHerbTask) action.getAction()).getTarget(), ((RequestHerbTask) action.getAction()).getSellHerb(), conversationId, participant, true));
                    } else if(action.getAction() instanceof BorrowGold){
                        //System.out.println("======before" + myAgent.getName() + myAgent.getGold().getGold() + " " + ((BorrowGold)action.getAction()).getGold().getGold());
                        myAgent.getGold().setGold(myAgent.getGold().getGold() - ((BorrowGold)action.getAction()).getGold().getGold());
                        //System.out.println("======after" + myAgent.getGold().getGold());
                        getAgent().addBehaviour(new SendMoney(myAgent, (BorrowGold)action.getAction(), conversationId, participant));
                    }
                } else {
                    Concept action = ((Result) ce).getAction();
                    Object obj = ((Result) ce).getValue();
                    AID sender = message.getSender();
                    if (obj instanceof Gold && action instanceof InformGold) {
                        myAgent.getGoldMap().put(((InformGold)action).getOwner(), (Gold) obj);
                        //System.out.println(myAgent.getName() + ": Got it! " + sender + "has gold " + ((Gold) obj).getGold());
                        if (!message.getSender().equals(myAgent.getAID())) {
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.setLanguage(new SLCodec().getName());
                            msg.setOntology(AlchemyOntology.getInstance().getName());
                            msg.addReceiver(sender);
                            myAgent.getContentManager().fillContent(msg, new Result(new InformGold(myAgent.getGold(), myAgent.getAID()), myAgent.getGold()));
                            msg.setSender(sender);
                            myAgent.send(msg);
                        }
                    } else if (obj instanceof BusyState) {
                        //.out.println("==test" + ((SendBusyState)action).getConversationId());
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        //String name = new SLCodec().getName();
                        msg.setLanguage(message.getLanguage());
                        msg.setOntology(AlchemyOntology.getInstance().getName());
                        msg.addReceiver(sender);
                        msg.setSender(myAgent.getAID());
                        //msg.setContent("busy");

                        msg.setConversationId(((SendBusyState)action).getConversationId());
                        ((SendBusyState) action).getBusyState().setBusy(myAgent.isBusy());
                        if (myAgent.isBusy())
                            System.out.println(myAgent.getName() + ": I'm busy, so can't accept task");
                        else
                            System.out.println(myAgent.getName() + ": I'm not busy, so I can accept task");

                        myAgent.getContentManager().fillContent(msg, new Result(action, ((SendBusyState) action).getBusyState()));
                        myAgent.send(msg);
                    }
                }
            } catch (OntologyException | Codec.CodecException e) {
                e.printStackTrace();
            }
        }
    }
}
