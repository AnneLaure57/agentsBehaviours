package fr.ul.miage.shop;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class MovieBuyerAgent  extends Agent{
	 // The title of the movie to buy
	 private String movieTitle;
	 
	// The list of known seller agents
	 private AID[] sellerAgents;

	 // Put agent initializations here
	 protected void setup() {
		// Printout a welcome message
		System.out.println("Je suis l'agent internaute " +
					    getLocalName() + " appelé aussi " + getAID().getName());
	
		// Get the title of the movie to buy as a start-up argument
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			
			movieTitle = (String) args[0];
			System.out.println("Je recherche "+ movieTitle);
			
			addBehaviour(new TickerBehaviour(this, 60000) {
				protected void onTick() {
					// Update the list of seller agents
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					//Type de service
					sd.setType("achat-films");
					template.addServices(sd);
					try {
						DFAgentDescription[] result = DFService.search(myAgent, template);
						sellerAgents = new AID[result.length];
						for (int i = 0; i < result.length; ++i) {
							sellerAgents[i] = result[i].getName();
						}
					}
					catch (FIPAException fe) {
						fe.printStackTrace();
					}
					// Perform the request
					//myAgent : protected variable 
					myAgent.addBehaviour(new ResponderBehaviour(myAgent));
				}
			} );
		 }
		 else {
			 // Make the agent terminate immediately
			 System.out.println("Are U drunk ? Pas de film correspondant." );
			 doDelete();
		 }
	 }
	 // Put agent clean-up operations here
	 protected void takeDown() {
		 // Printout a dismissal message
		 System.out.println("l'agent internaute " +getAID().getName()+" s'est arrêté.");
	 }
}
