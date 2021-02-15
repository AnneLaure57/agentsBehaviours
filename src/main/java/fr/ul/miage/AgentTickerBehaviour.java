package fr.ul.miage;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class AgentTickerBehaviour extends Agent {

 protected void setup() {
  System.out.println("Je suis le tout premier Agent : " +
    getLocalName() + " appelé aussi " + getAID().getName());

  addBehaviour(new TickerBehaviour(this, 1000) {
   @Override
   protected void onTick() {
    System.out.println("Salut, je suis Bob l'éponge");
   }
  });

 }
}
