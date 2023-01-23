package behaviours.student;


import agents.Student;
import behaviours.ReceiveResultBehaviour;
import behaviours.RequestActionBehaviour;
import jade.content.AgentAction;
import jade.core.AID;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/22 23:19
 * Description: Request other students for whether I can do this task
 */

public class RequestTask extends RequestActionBehaviour<Student, AgentAction> {

    public RequestTask(Student agent, AID participant, AgentAction action) {
        super(agent, participant, action);
    }

    @Override
    protected ReceiveResultBehaviour createResultBehaviour(String conversationId) {
        return new ReceiveTaskResponse(getMyAgent(), conversationId);
    }
}
