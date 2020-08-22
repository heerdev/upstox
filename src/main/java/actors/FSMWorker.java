package actors;


import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import entity.BarInput;
import entity.BarOHLC;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class FSMWorker extends AbstractBehavior<FSMWorker.Command> {

    private  static List<BarInput> barInputs = new ArrayList<>();
    private static AtomicInteger bar_num= new AtomicInteger(1);


    public interface  Command extends Serializable{}

    public static class GetBarInput implements Command {
        private static final long serialVersionUID = 1L;
        private BarInput barInput ;

        public GetBarInput(BarInput barInput) {
            this.barInput = barInput;

        }


        public BarInput getBarInput() {
            return barInput;
        }
    }

    public static class StartCommand implements Command {
        private static final long serialVersionUID = 1L;
        private String message;
        private ActorRef<OHLCController.Command> sender;

        public StartCommand(String message, ActorRef<OHLCController.Command> sender) {
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



    public static class GetBarDataCommand implements Command{
        private static final long serialVersionUID = 1L;
        private static List<BarInput> barInputs = new ArrayList<>();

        private ActorRef<OHLCController.Command> sender;

        public GetBarDataCommand(List<BarInput> barInputs, ActorRef<OHLCController.Command> sender) {
            this.barInputs = barInputs;
            this.sender = sender;
        }

        public List<BarInput> getBarInputs() {
            return barInputs;
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

                .onMessage(GetBarDataCommand.class, message->{

                    Map<String, List<BarInput>> maplist  =  message.getBarInputs().stream()
                                                    .collect(Collectors.groupingBy(BarInput::getSym, Collectors.toList()));
                    List<BarOHLC> barOHLCS = new ArrayList<>();
                    List<Double> tradevalue = new ArrayList<>();;
                    List<Double> tradeQuanity = new ArrayList<>();
                    List<BarInput>  bar = new ArrayList<>();
                    for (Map.Entry<String, List<BarInput>> entry: maplist.entrySet()) {

                        for (BarInput barInput: entry.getValue()) {
                            tradevalue.add(barInput.getProduct());
                            tradeQuanity.add(barInput.getQuantity());
                            bar.add(barInput);

                        }
                        BarOHLC barOHLC = new BarOHLC();
                        Comparator<BarInput> comparator=Comparator.comparing(BarInput::getTimestamp2);
                        barOHLC.setOpen(entry.getValue().stream().sorted(comparator).collect(Collectors.toList()).get(0).getProduct());

                        barOHLC.setClose(0);
                        barOHLC.setHigh(Collections.max(tradevalue));
                        barOHLC.setLow(Collections.min(tradevalue));
                        barOHLC.setVolume(tradeQuanity.stream().reduce(0.00, Double::sum));
                        barOHLC.setEvent("ohlc_notify");
                        barOHLC.setSymbol(entry.getKey());

                        barOHLC.setBar_num(bar_num.get());

                        barOHLCS.add(barOHLC);


                    }
                    bar_num.incrementAndGet();
                    System.out.println(barOHLCS.toString());
                    return this;
                })
                .build();
    }
}
