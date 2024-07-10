package main.java.ru.clevertec.check;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop {

    private final Map<Integer, Product> stock = new HashMap<>();

    public Shop(List<Product> stock) {
        stock.forEach(product -> this.stock.put(product.getId(), product));
    }

    public void addProductToStock(Product product) {
        stock.put(product.getId(), product);
    }

    public Boolean tryToPay(Check check, BigDecimal balance) {
        return check.getTotalWithDiscount().compareTo(balance) <= 0;
    }

    public Check generateCheck(Map<Integer, Integer> idsToBuy, Short discountCardNum) {
        List<Check.CheckPosition> checkPositions = idsToBuy.entrySet()
                .stream()
                .map(entry -> {
                    Product product = stock.get(entry.getKey());
                    if (product == null) throw new IllegalArgumentException("Неверный id для товара: " + entry.getKey());
                    Integer qty = entry.getValue();
                    return new Check.CheckPosition(
                            qty,
                            product.getDescription(),
                            product.getPrice(),
                            product.isWholesaleProduct(),
                            discountCardNum
                    );
                })
                .toList();
        return new Check(checkPositions, discountCardNum);
    }


}
