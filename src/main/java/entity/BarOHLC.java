package entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BarOHLC {


    @JsonProperty("o")
    private double open;
    @JsonProperty("h")
    private double high;
    @JsonProperty("l")
    private double low;
    @JsonProperty("c")
    private double close;

    private double volume;
    private String event;
    private String symbol;
    private int bar_num;

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
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
                "open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", event='" + event + '\'' +
                ", symbol='" + symbol + '\'' +
                ", bar_num=" + bar_num +
                '}';
    }
}
