package behaviours.student;

import agents.Student;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.*;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/22 21:00
 * Description: the student do some behaviour after getting msg
 */

public class StudentResponseBehaviour extends CyclicBehaviour {

    protected final Student myAgent;

    public StudentResponseBehaviour(Student myAgent) {
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
                ContentElement ce = myAgent.getContentManager().extractContent(message);
                if (ce instanceof Action) {
                    Action action = (Action) ce;
                    String conversationId = message.getConversationId();
                    AID sender = message.getSender();
                    if (action.getAction() instanceof BroadcastTaskAction) {
                        // add new request price tasks,
                        // in case seller agents will also changed when students get new shopping tasks
                        myAgent.setPriceTaskMap(new HashMap<>());
                        DFAgentDescription[] sellers = findSellers();
                        for(DFAgentDescription seller: sellers){
                            myAgent.getPriceTaskMap().put(new SendPriceList(new PriceList(seller.getName(), null)), null);
                        }

                        // add buy product tasks
                        BroadcastTaskAction bt = (BroadcastTaskAction)(action.getAction());
                        for(String product : bt.getProductNames()){
                            myAgent.getShopTaskMap().put(new SellProduct(new Product(product)), null);
                        }
                    }else if(action.getAction() instanceof RequestPriceTask){
                        RequestPriceTask rpt = (RequestPriceTask) action.getAction();
                        if(myAgent.getPriceTaskMap().get(rpt.getPriceTask()) != null?
                                (System.currentTimeMillis() - myAgent.getPriceTaskMap().get(rpt.getPriceTask()).getDatetime().getTime()> 10000):
                                (myAgent.getWantDo() == null || (!rpt.getPriceTask().equals(myAgent.getWantDo())
                                        || (myAgent.getTaskCount() >= rpt.getTaskCount() && (myAgent.getTaskCount() != rpt.getTaskCount() || myAgent.getName().compareTo(sender.getName()) > 0))))
                        ){
                            // response ok
                            rpt.setApprove(true);
                        }else{
                            // response refuse
                            rpt.setApprove(false);
                        }
                        System.out.println(myAgent.getName() + " REPLY " + sender.getName() + rpt.getPriceTask().getPriceList().getSellerAid().getName() + " PRICE TASK " + rpt.isApprove());
                        myAgent.addBehaviour(new ResponseTaskRequest(myAgent, rpt, conversationId, sender));
                    }else if(action.getAction() instanceof RequestShopTask){
                        RequestShopTask rst = (RequestShopTask) action.getAction();
                        if(myAgent.getShopTaskMap().get(rst.getShopTask()) != null?
                                (System.currentTimeMillis() - myAgent.getShopTaskMap().get(rst.getShopTask()).getDatetime().getTime()> 10000):
                                (myAgent.getWantDo() == null || (!rst.getShopTask().equals(myAgent.getWantDo())
                                        || (myAgent.getTaskCount() >= rst.getTaskCount() && (myAgent.getTaskCount() != rst.getTaskCount() || myAgent.getName().compareTo(sender.getName()) > 0))))
                        ){
                            // response ok
                            rst.setApprove(true);
                        }else{
                            // response refuse
                            rst.setApprove(false);
                        }
                        System.out.println(myAgent.getName() + " REPLY " + sender.getName() + " SHOP TASK " + rst.getShopTask().getProduct().getName() + " " + rst.isApprove());
                        myAgent.addBehaviour(new ResponseTaskRequest(myAgent, rst, conversationId, sender));
                    } else if(action.getAction() instanceof BroadcastPriceTask){
                        BroadcastPriceTask bpt = (BroadcastPriceTask) action.getAction();
                        // System.out.println(myAgent.getName() + ": Get broadcast that " + sender.getName() + " take price task - " + bpt.getPriceTask().getPriceList().getSellerAid().getName() + " - signature is: " + bpt.getSignature().getName());
                        // System.out.println("if contains？" + myAgent.getPriceTaskMap().containsKey(bpt.getPriceTask()));
                        myAgent.getPriceTaskMap().put(bpt.getPriceTask(), bpt.getSignature());
                    }else if(action.getAction() instanceof BroadcastShopTask){
                        BroadcastShopTask bst = (BroadcastShopTask) action.getAction();
                        myAgent.getShopTaskMap().put(bst.getShopTask(), bst.getSignature());
                    }else if(action.getAction() instanceof BroadcastPriceListAction){
                        BroadcastPriceListAction bpl = (BroadcastPriceListAction) action.getAction();
                        for(PriceItem priceItem : bpl.getSendPriceList().getPriceList().getPriceItems()){
                            myAgent.getPriceMap().putIfAbsent(priceItem.getProduct().getName(), new PriorityQueue<>());
                            myAgent.getPriceMap().get(priceItem.getProduct().getName()).add(priceItem);
                        }
                        myAgent.getPriceTaskMap().remove(bpl.getSendPriceList());
                        //System.out.println("TEST:" + myAgent.getPriceTaskMap());
                    }else if(action.getAction() instanceof BroadcastProductAction){
                        BroadcastProductAction bp = (BroadcastProductAction) action.getAction();
                        myAgent.getShopTaskMap().remove(bp.getSellProduct());
                        // System.out.println("TEST:" + myAgent.getShopTaskMap());
                    }
                }
            } catch (OntologyException | Codec.CodecException e) {
                e.printStackTrace();
            }
        }
    }

    private DFAgentDescription[] findSellers(){
        DFAgentDescription dfad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        DFAgentDescription[] studentServices = new DFAgentDescription[0];
        try {
            sd.setType("seller");
            dfad.addServices(sd);
            studentServices = DFService.search(myAgent, dfad);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        return studentServices;
    }
}
