package fr.ul.miage.shop;

import java.util.Hashtable;

import fr.ul.miage.test.ResponderBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MovieSellerAgent extends Agent{
	
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	
	//Service of Agent
	private String service;
	// The catalogue of books for sale (maps the title of a book to its price)
	 public Hashtable catalogue;
	 
	 // Put agent initializations here
	 protected void setup() {
		// Create the catalogue
		catalogue = new Hashtable();
		 
		Object[] args = getArguments();
		service = "vente";
		System.out.println("Salut, je suis l'agent distributeur "+this.getLocalName());
		this.registerService();
		
		// Perform the request
		//myAgent : protected variable -> renvoie à l'agent concerné qui correspond à l'agent Distributeur
		
		// Add the behaviour serving requests for offer from buyer agents
		//addBehaviour(new OfferRequests());
		// Add the behaviour serving purchase orders from buyer agents
		//addBehaviour(new PurchaseOrders());
		
		//Add Behaviour
		super.addBehaviour(new ResponderBehaviour(this));
	 }
	 
	 private void registerService() {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(this.getAID());
			
			ServiceDescription sd = new ServiceDescription();
			sd.setType(service);
			sd.setName(service);
			
			dfd.addServices(sd);
			try {
			    DFService.register(this, dfd);
			} catch (FIPAException e) {
			    System.err.println(getLocalName() +
					       " registration with DF unsucceeded. Reason: " + e.getMessage());
			    doDelete();
			}
		
	   }
	 
	 // Put agent clean-up operations here
	 protected void takeDown() {
		// Printout a dismissal message
		System.out.println("l'agent internaute " +getAID().getName()+" s'est arrêté.");
	 }
}
