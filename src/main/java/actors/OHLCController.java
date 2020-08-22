package actors;


import akka.actor.typed.ActorRef;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.BarInput;
import entity.BarOHLC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class OHLCController extends AbstractBehavior<OHLCController.Command> {

    private Object TIMER_KEY;

    public interface Command extends Serializable {}

    public static class InstructionCommand implements Command {
        public static final long serialVersionUID = 1L;
        private String message;

        public InstructionCommand(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class GenerateBarData implements Command{

        public static final long serialVersionUID = 1L;
        private String message;

        public GenerateBarData() {

        }

        public String getMessage() {
            return message;
        }
    }

    private OHLCController(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
        return Behaviors.setup(OHLCController::new);
    }

    private Map<ActorRef<FSMWorker.Command>, Integer> currentFSM;

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(InstructionCommand.class , command -> {
                    if (command.getMessage().equals("start")) {
                        currentFSM = new HashMap<>();
                        ActorRef<FSMWorker.Command> fsmActor = getContext().spawn(FSMWorker.create(), "WORKER_FSM");
                        readTradeDataAndSendToFSM(fsmActor);
                        currentFSM.put(fsmActor,0);

                    }
                    return Behaviors.withTimers(timer->{
                            timer.startTimerWithFixedDelay(TIMER_KEY,new GenerateBarData(), Duration.ofSeconds(15));
                        return this;
                    });
                })
                .onMessage(GenerateBarData.class, message->{
                    for (ActorRef<FSMWorker.Command> commandActorRef:currentFSM.keySet()) {
                        commandActorRef.tell(new FSMWorker.Command(null,"getBarData", getContext().getSelf()));
                    }
                    return this;
                })
                .build();
    }

    private void readTradeDataAndSendToFSM( ActorRef<FSMWorker.Command> fsmActor ) throws URISyntaxException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        BufferedReader reader;
        try {
            URL res = getClass().getClassLoader().getResource("trade.json");
            File file = Paths.get(res.toURI()).toFile();
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {


                // convert JSON string to Map
                BarInput barInput = mapper.readValue(line, BarInput.class);

                fsmActor.tell(new FSMWorker.Command(barInput, "",getContext().getSelf()));


                line = reader.readLine();
            }
            reader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
