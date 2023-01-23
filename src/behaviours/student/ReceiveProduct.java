package behaviours.student;

import agents.Student;
import behaviours.ReceiveResultBehaviour;
import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;
import ontology.*;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 14:36
 * Description: receive product from seller
 */

public class ReceiveProduct extends ReceiveResultBehaviour<Student> {

    public ReceiveProduct(Student agent, String conversationId) {
        super(agent, conversationId);
    }

    @Override
    protected void handleResult(Predicate predicate, AID participant) {
        Result result = (Result) predicate;
        if(result.getAction() instanceof SellProduct){
            Product product = (Product) result.getValue();
            getMyAgent().getProductList().add(product);

            getMyAgent().addBehaviour(new BroadcastProduct(getMyAgent(), new BroadcastProductAction((SellProduct) result.getAction())));

            System.out.println("Broadcasting: " + getMyAgent().getName() + ": I got [" + product.getName()+ "]");

            getMyAgent().getShopTaskMap().remove((SellProduct) result.getAction());

            getMyAgent().setBusy(false);
        }
    }
}
