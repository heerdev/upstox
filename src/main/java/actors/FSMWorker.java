package actors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import entity.BarOHLC;

import java.io.Serializable;

public class FSMWorker extends AbstractBehavior<FSMWorker.Command> {


    public static class Command implements Serializable{
        private static final long serialVersionUID = 1L;
        private BarOHLC barOHLC;

        public Command(BarOHLC barOHLC) {
            this.barOHLC = barOHLC;
        }

        public BarOHLC getBarOHLC() {
            return barOHLC;
        }

        public void setBarOHLC(BarOHLC barOHLC) {
            this.barOHLC = barOHLC;
        }
    }

       private  FSMWorker(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
        return Behaviors.setup(FSMWorker::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onAnyMessage(command->{
                    System.out.println("FSM got " +command.getBarOHLC());
                 return this;
                })
                .build();
    }
}
