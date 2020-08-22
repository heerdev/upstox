package actors;


import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import entity.BarInput;

import javax.swing.border.BevelBorder;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class OHLCController extends AbstractBehavior<OHLCController.Command> {

    private Object TIMER_KEY;
    private List<BarInput> barInputs = new ArrayList<>();

    public interface Command extends Serializable {}

    public static class Instruction implements Command {
        private static final long serialVersionUID = 1L;
        private String message;

        public Instruction(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class GenerateBarData implements Command {

        public static final long serialVersionUID = 1L;



    }
    private OHLCController(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
        return Behaviors.setup(OHLCController::new);
    }


    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
               .onMessage(Instruction.class, message->{

                   return Behaviors.withTimers(timers->{
                       timers.startTimerAtFixedRate(TIMER_KEY, new GenerateBarData(), Duration.ofSeconds(15));

                       return this;

                   });
               })
                .onMessage(GenerateBarData.class, message->{
                    ActorRef<FSMWorker.Command> fsmWorker  = getContext().spawn( FSMWorker.create(), "FSM_WORKER_"+ UUID.randomUUID());
                    fsmWorker.tell(new FSMWorker.GetBarDataCommand("startAgg", getContext().getSelf()));
                    return this;
                })

                .build();
    }



}
