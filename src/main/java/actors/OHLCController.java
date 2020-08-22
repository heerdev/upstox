package actors;



import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import entity.BarInput;


import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class OHLCController extends AbstractBehavior<OHLCController.Command> {

    private Object TIMER_KEY;
    private  List<BarInput> barInputs = new ArrayList<>();

    public interface Command extends Serializable {}

    public static class Instruction implements Command {
        private static final long serialVersionUID = 1L;
        private BarInput barInput;

        public Instruction(BarInput barInput) {
            this.barInput = barInput;
        }

        public BarInput getBarInput() {
            return barInput;
        }
    }

    public static class Result implements Command {
        private static final long serialVersionUID = 1L;
        private BarInput barInputs;

        public Result(BarInput barInputs) {
            this.barInputs = barInputs;
        }

        public BarInput getBarInputs() {
            return barInputs;
        }
    }


    public static class GenerateBarData implements Command {

        public static final long serialVersionUID = 1L;
        private List<BarInput> barInputs;

        public GenerateBarData(List<BarInput> barInputs) {
            this.barInputs = barInputs;
        }

        public List<BarInput> getBarInputs() {
            return barInputs;
        }
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
                   barInputs.add(message.getBarInput());
                  List<BarInput> copyBarInputs= new ArrayList<>();
                  copyBarInputs.addAll(barInputs);

                  return Behaviors.withTimers(timers -> {
                           timers.startTimerAtFixedRate(new GenerateBarData(copyBarInputs), Duration.ofSeconds(15));
                           return this;

                       });

               })
                .onMessage(GenerateBarData.class, message->{
                    ActorRef<FSMWorker.Command> fsmWorker  = getContext().spawn( FSMWorker.create(), "FSM_WORKER_"+ UUID.randomUUID());

                    fsmWorker.tell(new FSMWorker.GetBarDataCommand(message.getBarInputs(), getContext().getSelf()));
                    barInputs.clear();
                    return this;
                })

                .build();
    }



}
