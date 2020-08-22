import actors.OHLCController;
import actors.OHLCReader;
import akka.actor.typed.ActorSystem;

public class Main {

    public static void main(String[] args) {
        ActorSystem<OHLCReader.Command>  readMEssageOnFile = ActorSystem.create(OHLCReader.create(), "OHLCREADER");
        readMEssageOnFile.tell(new OHLCReader.ReadMessagesCommand("start"));


    }
}
