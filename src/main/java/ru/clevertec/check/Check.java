package main.java.ru.clevertec.check;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents shop-bill.
 *
 */
public class Check {

    private final LocalDateTime dateTime;
    private final List<CheckPosition> checkPositions = new ArrayList<>();
    private final Short discountCardNum;
    private final Float discountAmount;

    public Check(List<CheckPosition> checkPositions, Short discountCardNum) {
        this.checkPositions.addAll(checkPositions);
        this.dateTime = LocalDateTime.now();
        this.discountCardNum = discountCardNum;
        this.discountAmount = DiscountHelper.getDiscountAmountByCardNum(discountCardNum);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<CheckPosition> getCheckPositions() {
        return checkPositions;
    }

    public Short getDiscountCardNum() {
        return discountCardNum;
    }

    public Float getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getTotalPrice() {
        return checkPositions.stream()
                .map(checkPositions -> checkPositions.getPrice().multiply(BigDecimal.valueOf(checkPositions.getQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalDiscount() {
        return checkPositions.stream()
                .map(CheckPosition::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalWithDiscount() {
        return getTotalPrice().subtract(getTotalDiscount());
    }

    /**
     * Class that represent single position in a bill.
     *
     */
    public static class CheckPosition{
        private final Integer qty;
        private final String description;
        private final BigDecimal price;
        private final Boolean isWholesale;
        private final BigDecimal discount;

        public CheckPosition(Integer qty,
                             String description,
                             BigDecimal price,
                             Boolean isWholesale,
                             Short discountCardNum) {
            this.qty = qty;
            this.description = description;
            this.price = price;
            this.isWholesale = isWholesale;
            this.discount = isWholesale && qty >= 5
                    ? getDiscountForWholesale(price, qty)
                    : getDiscountForDiscountCard(price, qty, discountCardNum);

        }

        public Integer getQty() {
            return qty;
        }

        public Boolean getWholesale() {
            return isWholesale;
        }

        public String getDescription() {
            return description;
        }

        public BigDecimal getPrice() {
            return price.setScale(2, RoundingMode.HALF_UP);
        }

        public BigDecimal getDiscount() {
            return discount.setScale(2, RoundingMode.HALF_UP);
        }

        public BigDecimal getTotal() {
            return price.multiply(BigDecimal.valueOf(qty)).setScale(2, RoundingMode.HALF_UP);
        }

        @Override
        public String toString() {
            return "CheckPosition{" +
                    "qty=" + qty +
                    ", description='" + description + '\'' +
                    ", price=" + price +
                    ", isWholesale=" + isWholesale +
                    ", discount=" + discount +
                    '}';
        }

        private static BigDecimal getDiscountForWholesale(BigDecimal price, Integer qty) {
            return getDiscount(price, qty, DiscountHelper.getDiscountAmountForWholesale());
        }

        private static BigDecimal getDiscountForDiscountCard(BigDecimal price, Integer qty, Short cardNum) {
            return getDiscount(price, qty, DiscountHelper.getDiscountAmountByCardNum(cardNum));
        }

        private static BigDecimal getDiscount(BigDecimal price, Integer qtyInteger, Float discountPercentage) {
            BigDecimal qty = BigDecimal.valueOf(qtyInteger);
            BigDecimal hundred = BigDecimal.valueOf(100);
            BigDecimal discountMultiplier = BigDecimal.valueOf(discountPercentage).setScale(2, RoundingMode.HALF_UP).divide(hundred, RoundingMode.HALF_UP);
            return price
                    .multiply(qty)
                    .multiply(discountMultiplier)
                    .setScale(2, RoundingMode.HALF_UP);
        }

    }
}
