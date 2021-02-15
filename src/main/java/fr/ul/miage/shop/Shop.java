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
    	   agentController01 = containerController.createNewAgent("Sender", "fr.ul.miage.shop.MovieBuyerAgent", null);
    	   agentController01.start();
    	   agentController02 = containerController.createNewAgent("Receiver", "fr.ul.miage.shop.MovieSellerAgent", null);
    	   agentController02.start();
       	} catch (StaleProxyException e) {
            e.printStackTrace();
        }
	}
}