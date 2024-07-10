package main.java.ru.clevertec.check;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class CheckRunner {

    private static final String NOT_ENOUGH_MONEY_MESSAGE = "NOT ENOUGH MONEY";
    private static final String BAD_REQUEST_MESSAGE = "BAD REQUEST";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "INTERNAL SERVER ERROR";

    public static void main(String[] args) throws IOException {
        String pathToResultFile = InputParamsUtils.DEFAULT_RESULT_FILE_PATH;
        try {
            // Parse input params
            pathToResultFile = InputParamsUtils.getPathToResultFile(args);
            BigDecimal balance = InputParamsUtils.getBalanceDebitCardFromInputParams(args);
            Map<Integer, Integer> productIdsAndCount = InputParamsUtils.getIdsAndCountFromInputParams(args);
            Short discountCard = InputParamsUtils.getDiscountCardFromInputParams(args);
            String pathToProductFile = InputParamsUtils.getPathToProductFile(args);

            // Create shop with products from file and init discount cards
            DiscountHelper.init();
            Shop shop = new Shop(ProductReader.getAllProducts(pathToProductFile));

            // Try to buy products from params values
            Check check = shop.generateCheck(productIdsAndCount, discountCard);
            Boolean isSuccessPayment = shop.tryToPay(check, balance);

            // Generate response
            if (isSuccessPayment) {
                FileUtils.generateSuccessResponse(check, pathToResultFile);
                System.out.println("Successful payment!");
            } else {
                FileUtils.generateErrorResponse(NOT_ENOUGH_MONEY_MESSAGE, pathToResultFile);
                System.out.println("Payment error: not enough money!");
            }
        }
        // ERRORS
        catch (IllegalArgumentException e) {
            FileUtils.generateErrorResponse(BAD_REQUEST_MESSAGE, pathToResultFile);
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            FileUtils.generateErrorResponse(INTERNAL_SERVER_ERROR_MESSAGE, pathToResultFile);
            System.out.println(e.getMessage());
        }
    }

}