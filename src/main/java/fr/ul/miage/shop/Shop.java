package fr.ul.miage.shop;

import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.*;

public class Shop {
	
	public static void main(String... args) throws InterruptedException
	{
		//Put the ControllerAgents in comments to test here
		
       Runtime runtime = Runtime.instance();
       Profile profile = new ProfileImpl();
       profile.setParameter(Profile.MAIN_HOST, "localhost");
       profile.setParameter(Profile.GUI, "true");
       ContainerController containerController = runtime.createMainContainer(profile);

       AgentController agentController01,agentController02;
       
       try {
    	   agentController01 = containerController.createNewAgent("Internaute", "fr.ul.miage.shop.MovieBuyerAgent", new Object[] {"Lord of rings"});
    	   agentController01.start();
    	   agentController02 = containerController.createNewAgent("Distributeur", "fr.ul.miage.shop.MovieSellerAgent", new Object[] {"vente"});
    	   agentController02.start();
       	} catch (StaleProxyException e) {
            e.printStackTrace();
        }
	}
}