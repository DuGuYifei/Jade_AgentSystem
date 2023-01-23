package tech.liuyifei.migration.agents;


import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.Agent;
import jade.core.Location;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.List;
import lombok.Getter;
import lombok.Setter;
import tech.liuyifei.migration.behaviours.MigratingBehaviour;

import java.util.logging.Level;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/10/30 2:39
 * Description: agent migrating through the environment 代理在环境中迁移
 */

public class MigratingAgent extends Agent {

    @Getter
    private List locations;

    @Getter
    private Location startLocation;

    @Getter
    @Setter
    private Integer idLocation;

    private final MigratingBehaviour behaviour = new MigratingBehaviour(this);

    @Override
    protected void setup() {
        super.setup();
        //add behaviours
        addBehaviour(behaviour);
        startLocation = here();
        idLocation = 1;
        //System.out.println(startLocation);
    }

    @Override
    protected void afterMove() {
        System.out.println("Here I am " + here());
        if(here().equals(startLocation))
        {
            System.out.println("back");
            behaviour.stop();
        }
        //restore state
        //resume threads
    }
    @Override
    protected void beforeMove() {
        System.out.println("leaving");
        //stop threads
        //save state
    }


    public void getContainer(){
        QueryPlatformLocationsAction query;
        query = new QueryPlatformLocationsAction();

        ContentManager cm = getContentManager();
        cm.registerLanguage(new SLCodec());
        cm.registerOntology(MobilityOntology.getInstance());

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        String name = new SLCodec().getName();

        request.setLanguage(name);
        request.setOntology(MobilityOntology.getInstance().getName());
        request.addReceiver(getAMS());
        Action action = new Action(getAMS(), query);

        try{
            cm.fillContent(request, action);
            send(request);
        }catch (Codec.CodecException | OntologyException ex) {
            System.out.println(Level.WARNING + "\n" + ex.getMessage() + "\n" + ex);
        }

        MessageTemplate mt = MessageTemplate.MatchSender(getAMS());
        ACLMessage msg = blockingReceive(mt);

        try{
            ContentElement ce = cm.extractContent(msg);
            locations = ((Result) ce).getItems();
        } catch (OntologyException | Codec.CodecException e) {
            e.printStackTrace();
        }
        //System.out.println("locations:" + locations);
    }
}
