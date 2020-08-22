package actors;


import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import entity.BarInput;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FSMWorker extends AbstractBehavior<FSMWorker.Command> {

    private static List<BarInput> barInputs = Collections.synchronizedList(new ArrayList<>());  ;

    public interface  Command extends Serializable{}

    public static class GetBarInput implements Command {
        private static final long serialVersionUID = 1L;
        private BarInput barInput;

        public GetBarInput(BarInput barInput) {
            this.barInput = barInput;

        }


        public BarInput getBarInput() {
            return barInput;
        }
    }



    public static class GetBarDataCommand implements Command{
        private static final long serialVersionUID = 1L;
        private String message;
        private ActorRef<OHLCController.Command> sender;

        public GetBarDataCommand( String message, ActorRef<OHLCController.Command> sender) {
            this.message = message;
            this.sender = sender;
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
                .onMessage(GetBarInput.class,message ->{
                    barInputs.add(message.barInput);
                    System.out.println("GET +++" + barInputs.toString());
                    return this;
                })
                .onMessage(GetBarDataCommand.class, message->{
                    System.out.println(barInputs.toString());
                    return this;
                })
                .build();
    }
}
