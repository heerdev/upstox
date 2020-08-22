package entity;

import com.fasterxml.jackson.annotation.JsonInclude;

public class BarOHLC {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TradeAggregate tradeAggregate;
    private String event;
    private String symbol;
    private int bar_num;

    public TradeAggregate getTradeAggregate() {
        return tradeAggregate;
    }

    public void setTradeAggregate(TradeAggregate tradeAggregate) {
        this.tradeAggregate = tradeAggregate;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getBar_num() {
        return bar_num;
    }

    public void setBar_num(int bar_num) {
        this.bar_num = bar_num;
    }

    @Override
    public String toString() {
        return "BarOHLC{" +
                "tradeAggregate=" + tradeAggregate +
                ", event='" + event + '\'' +
                ", symbol='" + symbol + '\'' +
                ", bar_num='" + bar_num + '\'' +
                '}';
    }
}
