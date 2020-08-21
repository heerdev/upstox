package actors;


import akka.actor.typed.ActorRef;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.BarOHLC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;


public class ReadDataWorker extends AbstractBehavior<ReadDataWorker.Command> {

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

    private ReadDataWorker(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
        return Behaviors.setup(ReadDataWorker::new);
    }


    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(InstructionCommand.class , command -> {
                    if (command.getMessage().equals("start")) {
                        ActorRef<FSMWorker.Command> fsmActor = getContext().spawn(FSMWorker.create(), "WORKER_FSM");
                        readTradeDataAndSendToFSM(fsmActor);

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
                BarOHLC barOHLC = mapper.readValue(line, BarOHLC.class);

                fsmActor.tell(new FSMWorker.Command(barOHLC));


                line = reader.readLine();
            }
            reader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
