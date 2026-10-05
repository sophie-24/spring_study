package tobyspring.tobyspring;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private Long orderId;
    private String currency;
    private BigDecimal foreignCurrencyAmount;
    private BigDecimal exRate;
    private BigDecimal convertedAmount;
    private LocalDateTime validUntil;

    /*자바 객체에 데이터를 집어넣는 두 가지 방식
    * 1. 기본 생성자 + Setter 방식
    * : 빈 객체를 먼저 생성한 뒤, 나중에 세터 메서드를 하나씩 호출해 값을 집어넣는 방식
    * 2. 생성자 방식
    * : 객체를 만드는 시점(생성 동시에)에 필요한 값을 한 번에 다 집어넣는 방식
    *
    * (2) 생성자 방식을 권하는 이유 : 객체 생성과 동시에 값을 세팅하는 것이 훨씬 편리하기 때문
    * */

    //생성자 등등 만드는 단축 키 : alt + INS
    public Payment(Long orderId, String currency, BigDecimal foreignCurrencyAmount, BigDecimal exRate, BigDecimal convertedAmount, LocalDateTime validUntil) {
        this.orderId = orderId;
        this.currency = currency;
        this.foreignCurrencyAmount = foreignCurrencyAmount;
        this.exRate = exRate;
        this.convertedAmount = convertedAmount;
        this.validUntil = validUntil;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getForeignCurrencyAmount() {
        return foreignCurrencyAmount;
    }

    public BigDecimal getExRate() {
        return exRate;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "orderId=" + orderId +
                ", currency='" + currency + '\'' +
                ", foreignCurrencyAmount=" + foreignCurrencyAmount +
                ", exRate=" + exRate +
                ", convertedAmount=" + convertedAmount +
                ", validUntil=" + validUntil +
                '}';
    }
}
