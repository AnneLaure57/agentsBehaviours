package fr.ul.miage.test;

import java.util.Iterator;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiverAgent extends Agent{
	
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	
	 private String service;
	    
    @Override
    protected void setup() {
		Object[] args = getArguments();
		//service = (String) args[0];
		service = "Inventaire";
		System.out.println("Hello. My name is "+this.getLocalName());
		this.registerService();
		
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
		
}
  