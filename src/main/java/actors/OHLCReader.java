package actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.BarInput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public class OHLCReader extends AbstractBehavior<OHLCReader.Command> {
    private Object TIMER_KEY;

    public interface Command extends Serializable{}
    public static class ReadMessagesCommand implements Command {
        public static final long serialVersionUID = 1L;
        private String message;

        public ReadMessagesCommand(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
    private OHLCReader(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
        return Behaviors.setup(OHLCReader::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(ReadMessagesCommand.class , command -> {
                    if (command.getMessage().equals("start")) {

                        ActorRef<OHLCController.Command> fsmActor = getContext().spawn(OHLCController.create(), "OHLCController");
                        readTradeDataAndSendToFSM(fsmActor);


                    }
                    return this;
                })
                .build();
    }


    private void readTradeDataAndSendToFSM( ActorRef<OHLCController.Command> fsmActor ) throws URISyntaxException {
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


                fsmActor.tell(new OHLCController.Instruction(barInput));


                line = reader.readLine();
            }
            reader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
