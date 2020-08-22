package entity;



import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class BarInput {
    private String sym;
    @JsonProperty("T")
    private String trade;
    @JsonProperty("P")
    private double product;
    @JsonProperty("Q")
    private double quantity;
    @JsonProperty("TS")
    private double timestamp;

    private String side;
    @JsonProperty("TS2")
    private Timestamp timestamp2;


    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public double getProduct() {
        return product;
    }

    public void setProduct(double product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Timestamp getTimestamp2() {
        return timestamp2;
    }

    public void setTimestamp2(Timestamp timestamp2) {
        this.timestamp2 = timestamp2;
    }

    @Override
    public String toString() {
        return "BarInput{" +
                "sym='" + sym + '\'' +
                ", trade='" + trade + '\'' +
                ", product=" + product +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                ", side='" + side + '\'' +
                ", timestamp2=" + timestamp2 +
                '}';
    }

}
