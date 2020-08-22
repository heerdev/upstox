package actors;


import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import entity.BarInput;


import java.io.Serializable;

public class FSMWorker extends AbstractBehavior<FSMWorker.Command> {


    public static class Command implements Serializable{
        private static final long serialVersionUID = 1L;
        private BarInput barInput;
        private String message;
        private ActorRef<OHLCController.Command> sender;

        public Command(BarInput barInput, String message, ActorRef<OHLCController.Command> sender) {
            this.barInput = barInput;
            this.message = message;
            this.sender = sender;
        }

        public BarInput getBarInput() {
            return barInput;
        }

        public String getMessage() {
            return message;
        }

        public ActorRef<OHLCController.Command> getSender() {
            return sender;
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
                    System.out.println("FSM got " +command.getBarInput());
                 return this;
                })
                .build();
    }
}
