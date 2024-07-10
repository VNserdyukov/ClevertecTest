package main.java.ru.clevertec.check;

import java.math.BigDecimal;
import java.util.*;

/**
 * Class to process app-input params
 *
 */
public class InputParamsUtils {

    public static final String DEFAULT_RESULT_FILE_PATH = "result.csv";

    private static final String DISCOUNT_CARD_PARAM_NAME = "discountCard";
    private static final String BALANCE_CARD_PARAM_NAME = "balanceDebitCard";
    private static final String PATH_TO_PRODUCTS_PARAM_NAME = "pathToFile";
    private static final String PATH_TO_RESULT_PARAM_NAME = "saveToFile";
    private static final String DISCOUNT_CARD_PATTERN = "\\d{4}";
    private static final String BALANCE_CARD_PATTERN = "[+-]?\\d*(\\.?\\d{2})?";
    private static final String PRODUCTS_ID_AND_COUNT_PATTERN = "\\d*-\\d*";
    private static final String DISCOUNT_CARD_PATTERN_ERROR_MESSAGE = "Неверный формат номера дисконтной карты: ";
    private static final String BALANCE_CARD_PATTERN_ERROR_MESSAGE = "Неверный баланс карты: ";
    private static final String PRODUCTS_ID_AND_COUNT_PATTERN_ERROR_MESSAGE = "Ошибка формата id-quantity!";
    private static final String BALANCE_CARD_NOT_FOUND_MESSAGE = "Не задан баланс карты!";
    private static final String PATH_TO_PRODUCTS_NOT_FOUND_MESSAGE = "Не найден путь до файла с продуктами!";

    private InputParamsUtils() {}

    public static Short getDiscountCardFromInputParams(String[] args) {
        String discountCardNumString = Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(arg -> arg.startsWith(DISCOUNT_CARD_PARAM_NAME))
                .map(discountCard -> discountCard.replaceFirst(DISCOUNT_CARD_PARAM_NAME + "=", ""))
                .findFirst()
                .orElse(null);
        if (discountCardNumString != null &&
                !discountCardNumString.matches(DISCOUNT_CARD_PATTERN)) {
            throw new IllegalArgumentException(DISCOUNT_CARD_PATTERN_ERROR_MESSAGE + discountCardNumString);
        }
        return discountCardNumString == null ? null : Short.valueOf(discountCardNumString);
    }

    public static BigDecimal getBalanceDebitCardFromInputParams(String[] args) {
        String balanceDebitCardString = Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(arg -> arg.startsWith(BALANCE_CARD_PARAM_NAME))
                .map(balanceDebitCard -> balanceDebitCard.replaceFirst(BALANCE_CARD_PARAM_NAME + "=", ""))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(BALANCE_CARD_NOT_FOUND_MESSAGE));
        if (!balanceDebitCardString.matches(BALANCE_CARD_PATTERN)) {
            throw new IllegalArgumentException(BALANCE_CARD_PATTERN_ERROR_MESSAGE + balanceDebitCardString);
        }
        return BigDecimal.valueOf(Double.parseDouble(balanceDebitCardString));
    }

    public static Map<Integer, Integer> getIdsAndCountFromInputParams(String[] args) {
        List<String> idsAndCountStrings = Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(arg -> arg.matches(PRODUCTS_ID_AND_COUNT_PATTERN))
                .toList();

        Map<Integer, Integer> ids = new HashMap<>();
        for (String idAndCountString: idsAndCountStrings) {
            String[] massive = idAndCountString.split("-");
            ids.compute(Integer.valueOf(massive[0]), (key, oldValue) -> {
                try {
                    return oldValue == null ? Integer.parseInt(massive[1]) : oldValue + Integer.parseInt(massive[1]);
                }
                catch (Exception e) {
                    throw new IllegalArgumentException(PRODUCTS_ID_AND_COUNT_PATTERN_ERROR_MESSAGE);
                }
            });
        }
        return ids;
    }

    public static String getPathToProductFile(String[] args) {
        return Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(arg -> arg.startsWith(PATH_TO_PRODUCTS_PARAM_NAME))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(PATH_TO_PRODUCTS_NOT_FOUND_MESSAGE))
                .replaceFirst(PATH_TO_PRODUCTS_PARAM_NAME + "=", "");
    }

    public static String getPathToResultFile(String[] args) {
        return Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(arg -> arg.startsWith(PATH_TO_RESULT_PARAM_NAME))
                .findFirst()
                .orElse(DEFAULT_RESULT_FILE_PATH)
                .replaceFirst(PATH_TO_RESULT_PARAM_NAME + "=", "");
    }
}
