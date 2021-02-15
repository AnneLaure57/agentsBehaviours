package fr.ul.miage.shop;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class OfferRequests extends CyclicBehaviour {

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			// Message received. Process it
			String title = msg.getContent();
			ACLMessage reply = msg.createReply();
			Integer price = 55;
			if (price != null) {
				// The requested movie is available for sale. Reply with the price
				reply.setPerformative(ACLMessage.PROPOSE);
				reply.setContent(String.valueOf(price.intValue()));
			}
			else {
				// The requested movie is NOT available for sale.
				reply.setPerformative(ACLMessage.REFUSE);
				reply.setContent("non-disponible");
			}
			myAgent.send(reply);
		 } 
	}
}
