package behaviours.student;

import agents.Student;
import behaviours.ReceiveResultBehaviour;
import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import ontology.*;

import java.util.Date;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/22 23:59
 * Description: Receive whether other student approve I do this task
 */

public class ReceiveTaskResponse extends ReceiveResultBehaviour<Student> {

    public ReceiveTaskResponse(Student agent, String conversationId) {
        super(agent, conversationId);
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        Result result = (Result) predicate;
        if(result.getValue() instanceof RequestPriceTask){
            RequestPriceTask rpt = (RequestPriceTask) result.getValue();
            getMyAgent().setRequestTaskCount(getMyAgent().getRequestTaskCount() - 1);
            if(getMyAgent().getRequestTaskCount() == 0){
                if(getMyAgent().isRequestTaskApprove() && rpt.isApprove()){
                    getMyAgent().setBusy(true);
                }
                getMyAgent().setWantDo(null);
                getMyAgent().setAskingTask(false);
            }
            if(rpt.isApprove()){
                if(getMyAgent().getRequestTaskCount() == 0 && getMyAgent().isRequestTaskApprove()){
                    AgentSignature signature = new AgentSignature(getMyAgent().getName(), new Date());
                    getMyAgent().getPriceTaskMap().put(rpt.getPriceTask(), signature);
                    System.out.println("Broadcasting: " + getMyAgent().getName() + " taking price task - [" + rpt.getPriceTask().getPriceList().getSellerAid() + "]\n\n---------------------------\n");
                    //BroadcastTask(new BroadcastPriceTask(rpt.getPriceTask(), signature));
                    getMyAgent().addBehaviour(new BroadcastTask<>(getMyAgent(), new BroadcastPriceTask(rpt.getPriceTask(), signature)));
                    getMyAgent().addBehaviour(new RequestPriceList(getMyAgent(), rpt.getPriceTask().getPriceList().getSellerAid(), rpt.getPriceTask()));
                }
            }else{
                getMyAgent().setRequestTaskApprove(false);
            }
        }else if(result.getValue() instanceof RequestShopTask){
            RequestShopTask rst = (RequestShopTask) result.getValue();
            getMyAgent().setRequestTaskCount(getMyAgent().getRequestTaskCount() - 1);
            if(getMyAgent().getRequestTaskCount() == 0){
                if(getMyAgent().isRequestTaskApprove() && rst.isApprove()){
                    getMyAgent().setBusy(true);
                }
                getMyAgent().setWantDo(null);
                getMyAgent().setAskingTask(false);
            }
            if(rst.isApprove()){
                if(getMyAgent().getRequestTaskCount() == 0 && getMyAgent().isRequestTaskApprove()){
                    AgentSignature signature = new AgentSignature(getMyAgent().getName(), new Date());
                    getMyAgent().getShopTaskMap().put(rst.getShopTask(), signature);
                    System.out.println("Broadcasting: " + getMyAgent().getName() + " taking shopping task - [" + rst.getShopTask().getProduct().getName() + "]\n\n---------------------------\n");
                    // BroadcastTask(new BroadcastShopTask(rst.getShopTask(), signature));
                    getMyAgent().addBehaviour(new BroadcastTask<>(getMyAgent(),  new BroadcastShopTask(rst.getShopTask(), signature)));

                    //System.out.println(getMyAgent().getPriceMap());

                    AID target = getMyAgent().getPriceMap().get(rst.getShopTask().getProduct().getName()).peek().getSellerAID();
                    getMyAgent().addBehaviour(new RequestProduct(getMyAgent(), target, rst.getShopTask()));
                }
            }else{
                getMyAgent().setRequestTaskApprove(false);
            }
        }
    }

//    private <E extends AgentAction> void BroadcastTask(E broadCastTask){
//        getMyAgent().addBehaviour(
//                new FindServiceBehaviour(getMyAgent(), "student") {
//                    @Override
//                    protected void onResult(DFAgentDescription[] services) {
//                        if (services != null && services.length > 0) {
//                            for (DFAgentDescription dfag : services) {
//                                AID student = dfag.getName();
//                                if (student.equals(myAgent.getAID())) {
//                                    continue;
//                                }
//                                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//                                msg.setLanguage(new SLCodec().getName());
//                                msg.setOntology(ProductOntology.getInstance().getName());
//                                msg.addReceiver(student);
//                                try {
//                                    Action action = new Action(myAgent.getAID(), broadCastTask);
//                                    myAgent.getContentManager().fillContent(msg, action);
//                                    myAgent.send(msg);
//                                } catch (Codec.CodecException | OntologyException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                });
//    }

}
