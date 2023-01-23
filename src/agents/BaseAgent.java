package agents;


import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import lombok.Getter;
import ontology.ProductOntology;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By IDEA
 * Author: s188026 Yifei Liu
 * Date: 2022/11/3 23:56
 * Description: bazowa klasa dla agentów w projekcie 项目中代理的基类
 */

public class BaseAgent extends Agent {

    @Getter
    protected final List<String> activeConversationIds = new ArrayList<>();

    public BaseAgent() {
    }

    @Override
    protected void setup() {
        super.setup();
        getContentManager().registerLanguage(new SLCodec());
        getContentManager().registerOntology(ProductOntology.getInstance());
    }
}
