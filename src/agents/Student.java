package agents;

import behaviours.RegisterServiceBehaviour;
import behaviours.student.StudentBehaviour;
import behaviours.student.StudentResponseBehaviour;
import jade.content.AgentAction;
import lombok.Getter;
import lombok.Setter;
import ontology.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;


/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2023/1/17 11:44
 * Description: student agent who need go to finish task
 */

@Getter
@Setter
public class Student extends BaseAgent{

    private final HashMap<SellProduct, AgentSignature> shopTaskMap = new HashMap<>();
    private HashMap<SendPriceList, AgentSignature> priceTaskMap = new HashMap<>(); // not final, in case each time we got new task, the seller in the market will change.

    private boolean isAskingTask = false;
    private int requestTaskCount = 0;
    private boolean requestTaskApprove = true;
    private boolean isBusy = false;
    private AgentAction wantDo;

    private final HashMap<String, PriorityQueue<PriceItem>> priceMap = new HashMap<>();
    private final List<Product> productList = new ArrayList<>();

    int taskCount = 0;

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new RegisterServiceBehaviour(this, "student"));

        addBehaviour(new StudentResponseBehaviour(this));
        addBehaviour(new StudentBehaviour(this));
    }

    @Override
    public void doDelete(){
        System.out.println("\n--------------------");
        System.out.println(getName() + ": I got " + productList);
        super.doDelete();
    }
}
