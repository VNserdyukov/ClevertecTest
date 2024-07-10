package main.java.ru.clevertec.check;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductReader {


    private ProductReader() {}

    public static List<Product> getAllProducts(String pathToFile) throws IOException {
        List<String> productStrings = FileUtils.readLinesFromFile(pathToFile);
        List<Product> products = new ArrayList<>(productStrings.size());
        for (String productString: productStrings) {
            String[] fields = productString.split(";");
            double price = Double.parseDouble(fields[2].replaceAll(",", "."));
            boolean wholesale = Objects.equals(fields[4], "+");
            products.add(new Product.Builder()
                    .id(Integer.valueOf(fields[0]))
                    .description(fields[1])
                    .price(BigDecimal.valueOf(price))
                    .quantityInStock(Integer.parseInt(fields[3]))
                    .isWholesaleProduct(wholesale)
                    .build());
        }
        return products;
    }

}
