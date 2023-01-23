package liuyifei.alchemists.agents;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import liuyifei.alchemists.behaviours.RegisterServiceBehaviour;
import liuyifei.alchemists.datacontainer.Pair;
import liuyifei.alchemists.datacontainer.Tuple;
import liuyifei.alchemists.behaviours.mage.*;
import liuyifei.alchemists.ontology.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import liuyifei.alchemists.behaviours.FindServiceBehaviour;

import java.util.*;
import java.util.logging.Level;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/3 23:55
 * Description: agent kupujący produkty 代理购买产品 一名法师，他正在寻找合适的药草和药水。
 */

@Getter
@Setter
@ToString
public class Mage extends BaseAgent {

    private boolean isBusy = false;

    private final List<Recipe> recipeList = new ArrayList<>();

    private final HashMap<String, PriorityQueue<Tuple<AID>>> priceMap = new HashMap<>(); // herb|SID,price,quantity

    private final List<Potion> potionList = new ArrayList<>();

    private final List<HerbItem> herbList = new ArrayList<>();

    private final List<String> failBuyList = new ArrayList<>();
    private final List<String> noRecipeList = new ArrayList<>();

    private Gold gold; // golds Mage had at the beginning
    private HashMap<AID, Gold> goldMap = new HashMap<>();
    private int totalGold = 0;

    private final SequentialBehaviour behaviour = new SequentialBehaviour(this);

    public Mage() {
    }

    @SneakyThrows
    @Override
    protected void setup() {
        System.out.println("\n---------------------------\n");

        super.setup();
        addBehaviour(new RegisterServiceBehaviour(this, "mage"));
        //System.out.println(Arrays.toString(getArguments()));
        String[] args = ((String) getArguments()[0]).split("\\|");
        gold = new Gold(Integer.parseInt(args[0]));

        addBehaviour(new MageBehaviour(this));
        BroadcastGold();

        if(args.length > 1) {
            args = args[1].split(",");
            System.out.println(getName() + ": I need these potions from argument");
            int index = 1;
            List<String> potionShoppingList = new ArrayList<>();
            for (String arg : args) {
                potionShoppingList.add(arg);
                System.out.println(" " + index + ". " + arg);
                index++;
            }

            RequestRecipe(potionShoppingList);
            //RequestPrice();
            addBehaviour(behaviour);

//            addBehaviour(new MageWaitBehaviour(behaviour, "\n" + getName() + ": I finish shopping! Now I have\n " +
//                    "Potion: " + getPotionList()+ "\n " +
//                    "Herb: " + getHerbList() + "\n    " +
//                    "And fail to buy:" + getFailBuyList() + "\n    " +
//                    "And no recipe for:" + getNoRecipeList() + "\n " +
//                    "I remain " + getGold() + " golds\n" +
//                    "\nKill the Start@*** agent in the gui, will show the money each herbalist and alchemist earn today: "));
        }else{
            System.out.println(getName() + ": There is nothing I need to buy from arguments");
        }

        System.out.println("\n---------------------------\n");

    }

    public void DivideRequestPriceTasks(){
        SequentialBehaviour subSeq = new SequentialBehaviour();
        RequestPriceTogether(subSeq);
        addBehaviour(subSeq);
        addBehaviour(new MageWaitNextBehaviour(subSeq, this, "DivideShoppingTasks", "\n------------------------------\nFinish request price together, now divide buy herb task\n------------------------------\n"));
    }

    public void DivideShoppingTasks(){
        totalGold = gold.getGold();
        Iterator<Map.Entry<AID, Gold>> iterator = goldMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<AID, Gold> entry = iterator.next();
            totalGold += entry.getValue().getGold();
        }

        SequentialBehaviour sub = new SequentialBehaviour();
        int myGold = gold.getGold();
        for(Recipe recipe : recipeList){
            //System.out.println("++++++++++问题" + myGold);
            SequentialBehaviour subSeq = new SequentialBehaviour();
            myGold = BuyHerbTogether(recipe, subSeq, myGold);
            sub.addSubBehaviour(subSeq);
            sub.addSubBehaviour(new MageWaitBehaviour(subSeq, "\n------------------------------\nFinish shopping together for " + recipe.getPotionName() + "\n------------------------------", this, true));
        }
        addBehaviour(sub);
        addBehaviour(new MageWaitNextBehaviour(sub, this, "FinishAll", "\n------------------------------\nFinish shopping together\n------------------------------\n"));
    }

    public void FinishAll(){
        System.out.println("\n" + getName() + ": I finish shopping! Now I have\n " +
                "Potion: " + getPotionList() + "\n " +
                "Herb: " + getHerbList() + "\n    " +
                "And fail to buy:" + getFailBuyList() + "\n    " +
                "And no recipe for:" + getNoRecipeList() + "\n " +
                "\nKill the Start@*** agent in the gui, will show the money each herbalist and alchemist earn today and remain for mages: ");
    }

    @Override
    public void doDelete(){
        System.out.println(getName() + ": I remain " + getGold().getGold() + " golds.");
        super.doDelete();
    }

//    private boolean bug = true;

    private int BuyHerbTogether(Recipe recipe, SequentialBehaviour subSeq, int myGold){
        List<PriceItem> lackHerbList = new ArrayList<>();
//        if(bug){
//            bug = false;
//        }else{
//            return;
//        }

        boolean canBought = false;
        try {
            canBought = canBuy(recipe);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        if (canBought) {
            AID alchemist = null;

            int mageId = 0;
            DFAgentDescription dfad2 = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            DFAgentDescription[] mageServices = new DFAgentDescription[0];
            try {
                sd.setType("mage");
                dfad2.addServices(sd);
                mageServices = DFService.search(this, dfad2);
            } catch (FIPAException e) {
                e.printStackTrace();
            }

            // buy herbs
            for (RecipeItem recipeItemItem: recipe.getHerbNameList()) {
                Pair<String, Integer> herbQuantity = recipeItemItem.getItem();
                String herb = herbQuantity.getX();
                Object quantityO = herbQuantity.getY();
                Long quantityOl = (Long)quantityO;
                Integer demandNum = quantityOl.intValue();

                PriorityQueue<Tuple<AID>> pq= priceMap.get(herb);

                List<Tuple<AID>> willRemove = new ArrayList<>();

                AID onlyAlchemist = null;
                for(Tuple<AID> tuple : pq) {
                    AID seller = tuple.getX();
                    int sellerQuantity = tuple.getZ();

                    int buyNum;
                    if (sellerQuantity >= demandNum) {
                        buyNum = demandNum;
                        demandNum = 0;
                    } else {
                        buyNum = sellerQuantity;
                        demandNum -= sellerQuantity;
                    }

                    DFAgentDescription dfad = new DFAgentDescription();
                    sd.setType("herbalist");
                    dfad.addServices(sd);
                    DFAgentDescription[] services = new DFAgentDescription[0];
                    try {
                        services = DFService.search(Mage.this, dfad);
                    } catch (FIPAException e) {
                        e.printStackTrace();
                    }
                    boolean isHerbalist = false;
                    for(DFAgentDescription d : services){
                        if(d.getName().equals(seller)){
                            isHerbalist = true;
                            break;
                        }
                    }
                    if(isHerbalist){
                        int moneyPay = tuple.getY() * buyNum;
                        SellHerb action = new SellHerb(new Herb(herb), moneyPay, buyNum);

                        Iterator<Map.Entry<AID, Gold>> iterator = goldMap.entrySet().iterator();
                        System.out.println(myGold + " " + moneyPay + herb + " " + buyNum);

                        while(myGold < moneyPay){
                            Map.Entry<AID, Gold> entry = iterator.next();
                            if(entry.getKey().getName().equals(getAID().getName())){
                                continue;
                            }
                            if(entry.getValue().getGold() >= moneyPay - myGold){
                                subSeq.addSubBehaviour(new RequestMoney(this, entry.getKey(), new BorrowGold(new Gold(moneyPay - myGold))));
                                entry.getValue().setGold(entry.getValue().getGold() - (moneyPay - myGold));
                                myGold = moneyPay;
                            }else{
                                subSeq.addSubBehaviour(new RequestMoney(this, entry.getKey(), new BorrowGold(new Gold(entry.getValue().getGold()))));
                                myGold += entry.getValue().getGold();
                                entry.getValue().setGold(0);
                            }
                        }

                        //gold.setGold(gold.getGold() - moneyPay);
                        //totalGold -= moneyPay;
                        myGold -= moneyPay;

                        int lenMage = mageServices.length;
                        if(mageId == lenMage){
                            mageId = 0;
                        }
                        SequentialBehaviour subOfSub = new SequentialBehaviour();
                        for(; mageId < lenMage; mageId++){
                            if(mageServices[mageId].getName().equals(getAID())){
                                if(!isBusy) {
                                    RequestHerbBehaviour request = new RequestHerbBehaviour(Mage.this, seller, action);
                                    subOfSub.addSubBehaviour(request);
                                    mageId++;
                                    break;
                                }
                            }else if(!CheckBusy(mageServices[mageId].getName())){
                                subOfSub.addSubBehaviour(new SendRequestHerbTask(this, mageServices[mageId].getName(), new RequestHerbTask(action, new Gold(moneyPay), seller)));
                                mageId++;
                                break;
                            }
                            if(mageId + 1 == lenMage){
                                mageId = 0;
                            }
                        }
                        subSeq.addSubBehaviour(subOfSub);
                        subSeq.addSubBehaviour(new MageWaitBehaviour(subOfSub, "    --------------Have bought this herb: [" + herb + "]--------------\n"));
                    }else{
                        if(onlyAlchemist == null) {
                            onlyAlchemist = seller;
                            alchemist = seller;
                            lackHerbList.add(new PriceItem(new Tuple<>(herb, tuple.getY(), buyNum)));
                        }else{
                            demandNum += buyNum;
                            continue;
                        }
                    }

                    tuple.setZ(tuple.getZ() - buyNum);

                    if(tuple.getZ() <= 0)
                        willRemove.add(tuple);

                    if(demandNum == 0)
                        break;
                }
                pq.removeAll(willRemove);
            }

            // go to alchemist to build potion
            if(alchemist == null){
                DFAgentDescription dfad = new DFAgentDescription();
                sd.setType("alchemist");
                dfad.addServices(sd);
                DFAgentDescription[] services = new DFAgentDescription[0];
                try {
                    services = DFService.search(Mage.this, dfad);
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
                alchemist = services[0].getName();
            }
            int pay = Integer.parseInt(ResourceBundle.getBundle("liuyifei.alchemists.configure").getString("alchemist.potionPrice"));

            for(PriceItem p_herb_price_num:lackHerbList){
                Tuple<String>herb_price_num = p_herb_price_num.getItem();
                Integer lackPrice = herb_price_num.getY();
                Integer lackNum = herb_price_num.getZ();
                pay += lackPrice * lackNum;
            }

            Iterator<Map.Entry<AID, Gold>> iterator = goldMap.entrySet().iterator();
            //.out.println("===" + goldMap + " " + pay + " " + recipe.getPotionName());

            while(myGold < pay){
                Map.Entry<AID, Gold> entry = iterator.next();
                if(entry.getKey().getName().equals(getAID().getName()) || entry.getValue().getGold() == 0){
                    continue;
                }
                if(entry.getValue().getGold() >= pay - myGold){
                    subSeq.addSubBehaviour(new RequestMoney(this, entry.getKey(), new BorrowGold(new Gold(pay - myGold))));
                    entry.getValue().setGold(entry.getValue().getGold() - (pay - myGold));
                    myGold = pay;
                }else{
                    subSeq.addSubBehaviour(new RequestMoney(this, entry.getKey(), new BorrowGold(new Gold(entry.getValue().getGold()))));
                    myGold += entry.getValue().getGold();
                    entry.getValue().setGold(0);
                }
            }

            //gold.setGold(gold.getGold() - pay);
            //totalGold -= pay;
            //System.out.println("===" + myGold + " " + pay);
            myGold -= pay;


            SellPotion action = new SellPotion(new Potion(recipe.getPotionName()), herbList, lackHerbList, pay);

            RequestPotionBehaviour request = new RequestPotionBehaviour(Mage.this, alchemist, action);

            SequentialBehaviour subOfSub = new SequentialBehaviour();
            subOfSub.addSubBehaviour(request);
            subSeq.addSubBehaviour(subOfSub);
            subSeq.addSubBehaviour(new MageWaitBehaviour(subOfSub, "--------------Have bought this potion: [" + recipe.getPotionName() + "]--------------\n"));
        }else{
            failBuyList.add(recipe.getPotionName());
        }
        return myGold;
    }

    private void RequestPriceTogether(SequentialBehaviour sub){
        SequentialBehaviour sb = new SequentialBehaviour();
        String mageService = "mage";
        String herbService = "herbalist";
        String alchemistService = "alchemist";
        DFAgentDescription dfad = new DFAgentDescription();
        DFAgentDescription dfad2 = new DFAgentDescription();
        DFAgentDescription dfad3 = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        try {
            sd.setType(mageService);
            dfad.addServices(sd);
            DFAgentDescription[] mageServices = DFService.search(this, dfad);
            sd.setType(herbService);
            dfad2.addServices(sd);
            DFAgentDescription[] herbServices = DFService.search(this, dfad2);
            sd.setType(alchemistService);
            dfad3.addServices(sd);
            DFAgentDescription[] alServices = DFService.search(this, dfad3);

            int i = 0;
            for(DFAgentDescription dfagHerb : herbServices){
                int lenMage = mageServices.length;
                AID targetHerbalist = dfagHerb.getName();
                if(i == lenMage){
                    i = 0;
                }
                for(; i < lenMage; i++){
                    if(mageServices[i].getName().equals(getAID())){
                        if(!isBusy) {
                            sb.addSubBehaviour(new RequestPriceBehaviour(this, targetHerbalist, new SendPriceList(new PriceList(targetHerbalist, new ArrayList<>()))));
                            i++;
                            break;
                        }
                    }else if(!CheckBusy(mageServices[i].getName())){
                        sb.addSubBehaviour(new SendRequestPriceTask(this, mageServices[i].getName(), new RequestPriceTask(new SendPriceList(new PriceList(targetHerbalist, new ArrayList<>())), targetHerbalist)));
                        i++;
                        break;
                    }
                    if(i + 1 == lenMage){
                        i = 0;
                    }
                }
            }
            for(DFAgentDescription dfagHerb : alServices){
                int lenMage = mageServices.length;
                AID targetHerbalist = dfagHerb.getName();
                if(i == lenMage){
                    i = 0;
                }
                for(; i < lenMage; i++){
                    if(mageServices[i].getName().equals(getAID())){
                        if(!isBusy) {
                            sb.addSubBehaviour(new RequestPriceBehaviour(this, targetHerbalist, new SendPriceList(new PriceList(targetHerbalist, new ArrayList<>()))));
                            i++;
                            break;
                        }
                    }else if(!CheckBusy(mageServices[i].getName())){
                        sb.addSubBehaviour(new SendRequestPriceTask(this, mageServices[i].getName(), new RequestPriceTask(new SendPriceList(new PriceList(targetHerbalist, new ArrayList<>())), targetHerbalist)));
                        i++;
                        break;
                    }
                    if(i + 1 == lenMage){
                        i = 0;
                    }
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        sub.addSubBehaviour(sb);
        //sub.addSubBehaviour(new MageWaitBehaviour(sb, "\n------------------------------\nFinish request price together, now divide shopping task\n------------------------------\n"));
    }

    private boolean CheckBusy(AID target){
        ContentManager cm = getContentManager();
        cm.registerLanguage(new SLCodec());
        cm.registerOntology(AlchemyOntology.getInstance());

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        String name = new SLCodec().getName();
        String conversationId = UUID.randomUUID().toString();
        //getActiveConversationIds().add(conversationId);
        request.setLanguage(name);
        request.setOntology(AlchemyOntology.getInstance().getName());
        request.addReceiver(target);
        request.setSender(getAID());
        //request.setConversationId(conversationId);
        try{
            BusyState bs = new BusyState(true);
            cm.fillContent(request, new Result(new SendBusyState(bs, conversationId), bs));
            send(request);
        }catch (Codec.CodecException | OntologyException ex) {
            System.out.println(Level.WARNING + "\n" + ex.getMessage() + "\n" + ex);
        }

        //System.out.println("==task"+conversationId + " " + getAID().getName() + " " + target.getName());

        //MessageTemplate mt = MessageTemplate.MatchSender(target);
        MessageTemplate mt = MessageTemplate.MatchConversationId(conversationId);
        //mt = MessageTemplate.and(mt, MessageTemplate.MatchOntology(MageOntology.getInstance().getName()));
        //mt = MessageTemplate.and(mt, MessageTemplate.MatchConversationId(conversationId));
        //mt = MessageTemplate.and(mt, MessageTemplate.MatchContent("busy"));
        //mt = MessageTemplate.and(mt, MessageTemplate.MatchLanguage(name));
        ACLMessage msg = blockingReceive(mt);
        boolean res = true;
        try{
            ContentElement ce = cm.extractContent(msg);
            res = ((BusyState)((Result) ce).getValue()).isBusy();
        } catch (OntologyException | Codec.CodecException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void BroadcastGold() {
        addBehaviour(
                new FindServiceBehaviour(this, "mage") {
                    @Override
                    protected void onResult(DFAgentDescription[] services) {
                        System.out.println("Broadcasting Gold\n\n---------------------------\n");
                        if (services != null && services.length > 0) {
                            for (DFAgentDescription dfag : services) {
                                AID mage = dfag.getName();
                                if (mage.equals(getAID())) {
                                    continue;
                                }
                                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                                msg.setLanguage(new SLCodec().getName());
                                msg.setOntology(AlchemyOntology.getInstance().getName());
                                msg.addReceiver(mage);
                                try {
                                    InformGold ig = new InformGold(gold, getAID());
                                    myAgent.getContentManager().fillContent(msg, new Result(ig, gold));
                                    send(msg);
                                } catch (Codec.CodecException | OntologyException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }


    private void RequestRecipe(List<String> potionShoppingList) {
        SequentialBehaviour subSeq = new SequentialBehaviour();
        for (String potion : potionShoppingList) {
            SequentialBehaviour subOfSub = new SequentialBehaviour();
            subOfSub.addSubBehaviour(
                    new FindServiceBehaviour(this, "recipeLibrary") {
                        @Override
                        protected void onResult(DFAgentDescription[] services) {
                            if (services != null && services.length > 0) {
                                AID recipeLibrary = services[0].getName();
                                SendRecipe action = new SendRecipe(new Recipe(potion, new ArrayList<>()));
                                RequestRecipeBehaviour request = new RequestRecipeBehaviour(Mage.this, recipeLibrary, action);
                                ((SequentialBehaviour) getParent()).addSubBehaviour(request);
                            }
                        }
                    });
            subSeq.addSubBehaviour(subOfSub);
        }
        behaviour.addSubBehaviour(subSeq);
        behaviour.addSubBehaviour(new MageWaitNextBehaviour(subSeq, this, "DivideRequestPriceTasks", "\n------------------------------\nNext step: Got recipes, now request price in market\n------------------------------\n"));
    }

//    public void RequestPrice() {
//        SequentialBehaviour subSeq1 = new SequentialBehaviour();
//        subSeq1.addSubBehaviour(
//                new FindServiceBehaviour(this, "herbalist") {
//                    @Override
//                    protected void onResult(DFAgentDescription[] services) {
//                        if (services != null && services.length > 0) {
//                            for (DFAgentDescription dfag : services) {
//                                AID herbalist = dfag.getName();
//                                List<PriceItem> pl = new ArrayList<>();
//                                SendPriceList action = new SendPriceList(new PriceList(herbalist, pl));
//                                RequestPriceBehaviour request = new RequestPriceBehaviour(Mage.this, herbalist, action);
//                                SequentialBehaviour subOfSub = new SequentialBehaviour();
//                                subOfSub.addSubBehaviour(request);
//                                ((SequentialBehaviour) getParent()).addSubBehaviour(subOfSub);
//                            }
//
//                        }
//                    }
//                });
//        behaviour.addSubBehaviour(subSeq1);
//        behaviour.addSubBehaviour(new MageWaitBehaviour(subSeq1, "\n------------------------------\nNext step: Finish request price from herbalists in market, now request alchemist.\n------------------------------\n"));
//        SequentialBehaviour subSeq2 = new SequentialBehaviour();
//        subSeq2.addSubBehaviour(
//                new FindServiceBehaviour(this, "alchemist") {
//                    @Override
//                    protected void onResult(DFAgentDescription[] services) {
//                        if (services != null && services.length > 0) {
//                            for (DFAgentDescription dfag : services) {
//                                AID alchemist = dfag.getName();
//                                List<PriceItem> pl = new ArrayList<>();
//                                SendPriceList action = new SendPriceList(new PriceList(alchemist, pl));
//                                RequestPriceBehaviour request = new RequestPriceBehaviour(Mage.this, alchemist, action);
//                                SequentialBehaviour subOfSub = new SequentialBehaviour();
//                                subOfSub.addSubBehaviour(request);
//                                ((SequentialBehaviour) getParent()).addSubBehaviour(subOfSub);
//                            }
//
//                        }
//                    }
//                });
//        behaviour.addSubBehaviour(subSeq2);
//        //behaviour.addSubBehaviour(new MageWaitBehaviour(subSeq2, "Finish request price in market, now go shopping.\n"));
//        behaviour.addSubBehaviour(new MageWaitNextBehaviour(subSeq2, this, "GoShopping", "\n------------------------------\nNext step: Finish request price in market, now go shopping.\n------------------------------\n"));
//    }

//    public void GoShopping() throws FIPAException {
//        for (Recipe recipe : recipeList) {
//            boolean canBought = canBuy(recipe);
//
//            if (canBought) {
//
//                AID alchemist = null;
//                // buy herbs
//                for (RecipeItem recipeItemItem: recipe.getHerbNameList()) {
//                    Pair<String, Integer> herbQuantity = recipeItemItem.getItem();
//                    String herb = herbQuantity.getX();
//                    Object quantityO = herbQuantity.getY();
//                    Long quantityOl = (Long)quantityO;
//                    Integer demandNum = quantityOl.intValue();
//
//                    PriorityQueue<Tuple<AID>> pq= priceMap.get(herb);
//
//                    List<Tuple<AID>> willRemove = new ArrayList<>();
//
//                    AID onlyAlchemist = null;
//                    for(Tuple<AID> tuple : pq) {
//                        AID seller = tuple.getX();
//                        int sellerQuantity = tuple.getZ();
//
//                        int buyNum;
//                        if (sellerQuantity >= demandNum) {
//                            buyNum = demandNum;
//                            demandNum = 0;
//                        } else {
//                            buyNum = sellerQuantity;
//                            demandNum -= sellerQuantity;
//                        }
//
//                        DFAgentDescription dfad = new DFAgentDescription();
//                        ServiceDescription sd = new ServiceDescription();
//                        sd.setType("herbalist");
//                        dfad.addServices(sd);
//                        DFAgentDescription[] services = DFService.search(Mage.this, dfad);
//                        boolean isHerbalist = false;
//                        for(DFAgentDescription d : services){
//                            if(d.getName().equals(seller)){
//                                isHerbalist = true;
//                                break;
//                            }
//                        }
//                        if(isHerbalist){
//                            int moneyPay = tuple.getY() * buyNum;
//                            SellHerb action = new SellHerb(new Herb(herb), moneyPay, buyNum);
//
//                            gold.setGold(gold.getGold() - moneyPay);
//
//                            RequestHerbBehaviour request = new RequestHerbBehaviour(Mage.this, seller, action);
//                            SequentialBehaviour subOfSub = new SequentialBehaviour();
//                            subOfSub.addSubBehaviour(request);
//                            behaviour.addSubBehaviour(subOfSub);
//                            behaviour.addSubBehaviour(new MageWaitBehaviour(subOfSub, "    --------------Have bought this herb: [" + herb + "]--------------\n"));
//                        }else{
//                            if(onlyAlchemist == null) {
//                                onlyAlchemist = seller;
//                                alchemist = seller;
//                                lackHerbList.add(new PriceItem(new Tuple<>(herb, tuple.getY(), buyNum)));
//                            }else{
//                                demandNum += buyNum;
//                                continue;
//                            }
//                        }
//
//                        tuple.setZ(tuple.getZ() - buyNum);
//
//                        if(tuple.getZ() <= 0)
//                            willRemove.add(tuple);
//
//                        if(demandNum == 0)
//                            break;
//                    }
//                    pq.removeAll(willRemove);
//                }
//
//                // go to alchemist to build potion
//                if(alchemist == null){
//                    DFAgentDescription dfad = new DFAgentDescription();
//                    ServiceDescription sd = new ServiceDescription();
//                    sd.setType("alchemist");
//                    dfad.addServices(sd);
//                    DFAgentDescription[] services = DFService.search(Mage.this, dfad);
//                    alchemist = services[0].getName();
//                }
//
//                int pay = Integer.parseInt(ResourceBundle.getBundle("liuyifei.alchemists.configure").getString("alchemist.potionPrice"));
//
//                for(PriceItem p_herb_price_num:lackHerbList){
//                    Tuple<String>herb_price_num = p_herb_price_num.getItem();
//                    Integer lackPrice = herb_price_num.getY();
//                    Integer lackNum = herb_price_num.getZ();
//                    pay += lackPrice * lackNum;
//                }
//
//                gold.setGold(gold.getGold() - pay);
//                SellPotion action = new SellPotion(new Potion(recipe.getPotionName()), herbList, lackHerbList, pay);
//                RequestPotionBehaviour request = new RequestPotionBehaviour(Mage.this, alchemist, action);
//
//                SequentialBehaviour subOfSub = new SequentialBehaviour();
//                subOfSub.addSubBehaviour(request);
//                behaviour.addSubBehaviour(subOfSub);
//                behaviour.addSubBehaviour(new MageWaitBehaviour(subOfSub, "--------------Have bought this potion: [" + recipe.getPotionName() + "]--------------\n"));
//            }else{
//                failBuyList.add(recipe.getPotionName());
//            }
//        }
//    }

    private AID SoldByAlchemist(AID compare) throws FIPAException {
        DFAgentDescription dfad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("alchemist");
        dfad.addServices(sd);
        DFAgentDescription[] services = DFService.search(Mage.this, dfad);
        for(DFAgentDescription d : services){
            if(d.getName().equals(compare)){
                return d.getName();
            }
        }
        return null;
    }

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
}
