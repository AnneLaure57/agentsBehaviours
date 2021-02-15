package fr.ul.miage.shop;

import java.util.Iterator;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RequestPerformer extends Behaviour {
	
	 private AID bestSeller; // The agent who provides the best offer
	 private int bestPrice; // The best offered price
	 private int repliesCnt = 0; // The counter of replies from seller agents
	 private MessageTemplate mt; // The template to receive replies
	 private int step = 0;
	 
	 DFAgentDescription dfd = new DFAgentDescription();

	@Override
	public void action() {
		String message = "mon message";
	    AID aid = new AID();
		switch (step) {
		 	case 0:
				// Send the cfp to all sellers
			DFAgentDescription[] result;
			try {
				result = DFService.search(myAgent, dfd);
				String out = "";
	            int i = 0;
	            String service = "";
	            while ((service.compareTo("distributeur") != 0) && (i < result.length)) {
	                DFAgentDescription desc = (DFAgentDescription) result[i];
	                Iterator iter2 = desc.getAllServices();
	                while (iter2.hasNext()) {
	                    ServiceDescription sd = (ServiceDescription) iter2.next();
	                    service = sd.getName();
	                    if (service.compareTo("distributeur") == 0) {
	                        aid = desc.getName();
	                        break;
	                    }
	                }
	                System.out.println(aid.getName());

	                sendMessage(message, aid);
	                i++;
	            }
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 step = 1;
		 break;
		 case 1:
			 // Receive all proposals/refusals from seller agents
			 ACLMessage reply = myAgent.receive(mt);
			 if (reply != null) {
				 // Reply received
				 if (reply.getPerformative() == ACLMessage.PROPOSE) {
					 // This is an offer
					 int price = Integer.parseInt(reply.getContent());
					 if (bestSeller == null || price < bestPrice) {
						 // This is the best offer at present
						 bestPrice = price;
						 bestSeller = reply.getSender();
					 }
				 }
				 repliesCnt++;
				 if (repliesCnt >= 2) {
					 // We received all replies
					 step = 2;
				 }
			 } else {
				 block();
			 }
		 break;
		 case 2:
			 // Send the purchase order to the seller that provided the best offer
			 ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			 order.addReceiver(bestSeller);
			 order.setConversationId("négocitaitons-films");
			 order.setReplyWith("achat"+System.currentTimeMillis());
			 myAgent.send(order);
			 // Prepare the template to get the purchase order reply
			 mt = MessageTemplate.and(MessageTemplate.MatchConversationId("négocitaitons-films"),
			 MessageTemplate.MatchInReplyTo(order.getReplyWith()));
			 step = 3;
		 break;
		 case 3:
			 // Receive the purchase order reply
			 reply = myAgent.receive(mt);
			 if (reply != null) {
				 // Purchase order reply received
				 if (reply.getPerformative() == ACLMessage.INFORM) {
					 // Purchase successful. We can terminate
					 System.out.println(message + "achat effectué avec succès");
					 System.out.println("Prix = "+bestPrice);
					 myAgent.doDelete();
				 }
				 step = 4;
			 }
			 else {
				 block();
			 }
			 break;
		 }
	}
	
	private void sendMessage(String mess, AID id) {
        try {
            ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
            aclMessage.addReceiver(id);

            aclMessage.setContent(mess);

            myAgent.send(aclMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	@Override
	public boolean done() {
		return ((step == 2 && bestSeller == null) || step == 4);
	}

}
