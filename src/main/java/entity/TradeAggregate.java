package entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradeAggregate {

    @JsonProperty("o")
    private double open;
    @JsonProperty("h")
    private double high;
    @JsonProperty("l")
    private double low;
    @JsonProperty("c")
    private double close;

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

    @Override
    public String toString() {
        return "TradeAggregate{" +
                "open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                '}';
    }
}
