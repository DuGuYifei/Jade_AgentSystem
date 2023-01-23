package behaviours.student;

import agents.Student;
import behaviours.ReceiveResultBehaviour;
import behaviours.RequestActionBehaviour;
import jade.core.AID;
import ontology.SellProduct;
import ontology.SendPriceList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 14:31
 * Description: request product from seller
 */

public class RequestProduct extends RequestActionBehaviour<Student, SellProduct> {

    public RequestProduct(Student agent, AID participant, SellProduct action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        System.out.println(getAgent().getName() + ": I want " + getAction().getProduct().getName() + " Dear [" + getParticipant().getName() + "]");

        return new ReceiveProduct(getMyAgent(), conversationId);
    }
}
