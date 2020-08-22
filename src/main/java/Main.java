import actors.OHLCController;
import akka.actor.typed.ActorSystem;

public class Main {

    public static void main(String[] args) {
        ActorSystem<OHLCController.Command>  actorSystem = ActorSystem.create(OHLCController.create(), "OHLCServer");
        actorSystem.tell(new OHLCController.InstructionCommand("start"));

    }
}
