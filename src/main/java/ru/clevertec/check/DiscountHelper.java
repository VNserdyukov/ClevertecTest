package main.java.ru.clevertec.check;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to work with all kinds of discounts.
 *
 */
public class DiscountHelper {

    private final static String PATH_TO_DISCOUNT_CSV = "./resources/discountCards.csv";
    private final static Float DEFAULT_DISCOUNT_AMOUNT_FOR_DISCOUNT_CARD = 2F;
    private final static Float DEFAULT_DISCOUNT_AMOUNT_WITHOUT_DISCOUNT_CARD = 0F;
    private final static Float DISCOUNT_AMOUNT_FOR_WHOLESALE = 10F;

    private static Map<Short, Float> discountCards;

    private DiscountHelper() {}

    public static void init() throws IOException {
        discountCards = new HashMap<>();
        List<String> discountStrings = FileUtils.readLinesFromFile(PATH_TO_DISCOUNT_CSV);
        discountStrings.forEach(discountString -> {
            String[] discountInfo = discountString.split(";");
            Short number = Short.valueOf(discountInfo[0]);
            Float discountAmount = Float.valueOf(discountInfo[1]);
            discountCards.put(number, discountAmount);
        });
    }

    public static Float getDiscountAmountByCardNum(Short cardNum) {
        return cardNum == null ?
                DEFAULT_DISCOUNT_AMOUNT_WITHOUT_DISCOUNT_CARD :
                discountCards.getOrDefault(cardNum, DEFAULT_DISCOUNT_AMOUNT_FOR_DISCOUNT_CARD);
    }

    public static Float getDiscountAmountForWholesale() {
        return DISCOUNT_AMOUNT_FOR_WHOLESALE;
    }

}
