package behaviours.student;

import agents.Student;
import behaviours.ActionBehaviour;
import jade.content.AgentAction;
import jade.content.Predicate;
import jade.content.onto.basic.Result;
import jade.core.AID;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/23 0:59
 * Description: response task request behaviour
 */


public class ResponseTaskRequest extends ActionBehaviour<AgentAction, Student> {

    public ResponseTaskRequest(Student agent, AgentAction action, String conversationId, AID participant) {
        super(agent, action, conversationId, participant);
    }

    @Override
    protected Predicate performAction() {
        return new Result(action, action);
    }
}
