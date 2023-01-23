package behaviours.student;

import agents.Student;
import behaviours.ReceiveResultBehaviour;
import behaviours.RequestActionBehaviour;
import jade.core.AID;
import ontology.SendPriceList;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 13:21
 * Description: Request price from Seller
 */

public class RequestPriceList extends RequestActionBehaviour<Student, SendPriceList> {

    public RequestPriceList(Student agent, AID participant, SendPriceList action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        System.out.println(getAgent().getName() + ": Which products you have and how much? Dear [" + getParticipant().getName() + "]");

        return new ReceivePriceList(getMyAgent(), conversationId);
    }
}
