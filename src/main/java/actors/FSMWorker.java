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
import java.util.List;

public class FSMWorker extends AbstractBehavior<FSMWorker.Command> {

    private List<BarInput> barInputs = new ArrayList<>();

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
                .onMessage(GetBarDataCommand.class,(command) ->{
                    System.out.println("GET +++" + barInputs.toString());
                    return this;
                })
                .onMessage(GetBarInput.class, (command)->{
                    if(command.getBarInput()!=null) {
                        barInputs = new ArrayList<>();
                        barInputs.add(command.getBarInput());
                        System.out.println("getting the barinput");
                    }
                 return this;
                })

                .build();
    }
}
