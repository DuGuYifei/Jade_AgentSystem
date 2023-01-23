# README

## Workfow
1. When a new TaskManager show up (**so new task managers can be added during process**), it will broadcast the products need to be bought.
2. All student will add this product to their own shop task containers. 
3. At the same time, when they hear new task manager' new tasks, they will update container of requesting price task. Because between two different task manager show up, the seller in the market may also change.
4. They will go through his price task container firstly. if there is a task without signature (AID + Timestamp) or with sinature but over 10s, he will ask everyone whether he can do it.
5. When others get this request, they will check whether it is true over 10s. And if two containers want to do this task at the same time, they will compare who have done tasks less. The less one will refuse the more one.
6. If get approve from every other student. Then he will take this task and broadcast to everyone he will do it.
7. For shopping task is the same, just it need to be done after finish request task.
8. When student get priceList from seller he will broadcast to everyone. Students who get the broadcast will add this list to his own knowledge and remove the task from tasks container.
9. When student get product will also broadcast, and other students will remove this task from tasks container.

## Code

1. Request task: e.g. request price task
   
   There is taskCount which can used for comparing by another student.
    ```java
    public class RequestPriceTask implements AgentAction {

        private SendPriceList priceTask;
        private boolean approve;
        private int taskCount;

    }
    ```

2. Signature
    ```java
    public class AgentSignature implements Concept {

    private String name;
    private Date datetime;

    }
    ```

3. Solve conflicts when two agents want to do the same task
   
   The one who have bigger TaskCount will answer ok for the request from another student. If the count number is same, the one who have bigger AID Name, will answer OK.
    ```java
    RequestPriceTask rpt = (RequestPriceTask) action.getAction();
    if(myAgent.getPriceTaskMap().get(rpt.getPriceTask()) != null?
            (System.currentTimeMillis() - myAgent.getPriceTaskMap().get(rpt.getPriceTask()).getDatetime().getTime() > 10000):
            (myAgent.getWantDo() == null || (!rpt.getPriceTask().equals(myAgent.getWantDo())
                    || (myAgent.getTaskCount() >= rpt.getTaskCount() && (myAgent.getTaskCount() != rpt.getTaskCount() || myAgent.getName().comp(sender.getName()) > 0))))
    ){
        // response ok
        rpt.setApprove(true);
    }else{
        // response refuse
        rpt.setApprove(false);
    }
    ```
    
4. Attributes of student
    ```java
    // shop task
    private final HashMap<SellProduct, AgentSignature> shopTaskMap = new HashMap<>();

    // price task
    // not final, in case each time we got new task, the seller in the market will change.
    private HashMap<SendPriceList, AgentSignature> priceTaskMap = new HashMap<>(); 


    // if this student has taken some tasks
    private boolean isBusy = false;

    // used for check whether he has same task want to do when he get request that other student want to do same task
    private AgentAction wantDo;

    // after get broadcast from other student or get pricelist from seller, he will update here.
    private final HashMap<String, PriorityQueue<PriceItem>> priceMap = new HashMap<>();

    // after get product from seller, he will update here
    private final List<Product> productList = new ArrayList<>();

    // used to compare with other student when they want to do same task
    int taskCount = 0;
    ```


5. Student have two important CyclicBehaviour behaviour:
   1. StudentBehaviour: which is go through his own task container
        ```java
        @Override
        public void action() {

            if(myAgent.isBusy() || myAgent.isAskingTask()){
                return;
            }

            // firstly request price task
            for (Map.Entry<SendPriceList, AgentSignature> entry : myAgent.getPriceTaskMap().entrySet()){
                if(CanTakeTask(entry, new RequestPriceTask(entry.getKey(), true, myAgent.getTaskCount()))) {
                    myAgent.setWantDo(entry.getKey());
                    return;
                }
            }

            // secondly shopping task
            // start after finish request price tasks
            if(myAgent.getPriceTaskMap().size() > 0){
                return;
            }
            for (Map.Entry<SellProduct, AgentSignature> entry : myAgent.getShopTaskMap().entrySet()){
                if(CanTakeTask(entry, new RequestShopTask(entry.getKey(), true, myAgent.getTaskCount()))) {
                    myAgent.setWantDo(entry.getKey());
                    return;
                }
            }
        }
        ```
   2. StudentResponseBehaviour: when student get some information from other student, they will have some reaction. One important thing here is this information should not be the response for his own request, so it need:
        ```java
        MessageTemplate mt = MessageTemplate.MatchAll();
        // So every time when he have request to other agents, he need add the conversation id to his ActiveConversationIds list.
        for (String id : myAgent.getActiveConversationIds()) {
            mt = MessageTemplate.and(mt, MessageTemplate.not(MessageTemplate.MatchConversationId(id)));
        }
        ```

6. The sort for product price:
    ```java
    // String is the name of product
    private final HashMap<String, PriorityQueue<PriceItem>> priceMap = new HashMap<>();
    ```
    In PriceItem:
    ```java
    public class PriceItem implements Concept, Comparable<PriceItem>{

        private Product product;
        private Integer price;
        private AID sellerAID;

        @Override
        public int compareTo(PriceItem o) {
            return price - o.price;
        }
    }
    ```
    So, in my previous version of project which has more complex algorithm of buying things (have limitation of money, recipe, herb quantity in market, herb price, can buy herb and build potion in only one Alchemist, Alchemist's herb backup), it looks like this:
    ```java
    public class PriceItem implements Concept {
        private Tuple<String> item;
    }

    public class Tuple<X> implements Comparable<Tuple<X>>{
        private X x;
        private Integer y; // price
        private Integer z; // quantity

        @Override
        public int compareTo(Tuple<X> o) {
            return this.getY().equals(o.getY()) ? o.getZ() - this.getZ() : this.getY() - o.getY();
        }
    }

    // check whether can buy
    private boolean canBuy(Recipe recipe) throws FIPAException {

        int cost = Integer.parseInt(ResourceBundle.getBundle("liuyifei.alchemists.configure").getString("alchemist.potionPrice"));
        int cost_herb = Integer.parseInt(ResourceBundle.getBundle("liuyifei.alchemists.configure").getString("alchemist.herbPrice"));
        boolean canBought = true;
        AID cmpAl = null;

        for (RecipeItem ri: recipe.getHerbNameList()) {
            Pair<String, Integer> herbQuantity = ri.getItem();
            String herb = herbQuantity.getX();
            if (!priceMap.containsKey(herb)) {
                canBought = false;
                break;
            } else {
                Object quantityO = herbQuantity.getY();
                long quantityOl = (long)quantityO;
                int quantity = (int)quantityOl;

                AID cmpAl_oneHerb = null;
                for(Tuple<AID> t : priceMap.get(herb)){
                    if(quantity > 0) {
                        AID tmp = SoldByAlchemist(t.getX());
                        if (tmp != null) {
                            if (cmpAl_oneHerb == null) {
                                if(cmpAl == null || cmpAl == tmp) {
                                    cmpAl_oneHerb = tmp;
                                    int tempQuantity = t.getZ() >= quantity? quantity : t.getZ();
                                    cost += cost_herb * tempQuantity;
                                    if (cost > totalGold) {
                                        canBought = false;
                                        break;
                                    }
                                    quantity -= tempQuantity;
                                }
                                continue;
                            } else if (cmpAl_oneHerb != tmp) {
                                continue;
                            }
                        }
                        int tempQuantity = t.getZ() >= quantity? quantity : t.getZ();
                        cost += t.getY() * tempQuantity;
                        if (cost > totalGold) {
                            canBought = false;
                            break;
                        }

                        quantity -= tempQuantity;
                    }
                }
                if(canBought){
                    if (cmpAl_oneHerb != null) {
                        if (cmpAl == null) {
                            cmpAl = cmpAl_oneHerb;
                        }
                    }
                }else{
                    break;
                }
            }
        }
        if(canBought) {
            totalGold -= cost;
        }
        return canBought;
    }
    ```

7. EnvStart agent class: which can help to give arguments from `configure.properties` and create all other agents needed. Also it can kill all created agents at the same time:
   ```java
    @Override
    public void doDelete(){
        int n = agentList.size();
        for (int i = 0; i < n; i++) {
            // agentList is a Jade list which contains the AgentController of all agents he created
            AgentController ac = (AgentController) agentList.get(i);
            try {
                ac.kill();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
        super.doDelete();
    }
   ```

   