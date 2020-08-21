import actors.ReadDataWorker;
import akka.actor.typed.ActorSystem;

public class Main {

    public static void main(String[] args) {
        ActorSystem<ReadDataWorker.Command>  actorSystem = ActorSystem.create(ReadDataWorker.create(), "FirstActorSystem");
        actorSystem.tell(new ReadDataWorker.InstructionCommand("start"));

    }
}
