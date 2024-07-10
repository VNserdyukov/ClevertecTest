package main.java.ru.clevertec.check;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;


/**
 * Class to work with files.
 *
 */
public class FileUtils {

    private final static DecimalFormat DECIMAL_FORMAT;
    private final static DecimalFormat DECIMAL_FORMAT_PERCENTAGE;
    private final static DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS;
    private final static String DECIMAL_PATTERN = "0.00$";

    static {
        DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols();
        DECIMAL_FORMAT_SYMBOLS.setDecimalSeparator(',');
        DECIMAL_FORMAT = new DecimalFormat(DECIMAL_PATTERN, DECIMAL_FORMAT_SYMBOLS);
        DECIMAL_FORMAT_PERCENTAGE = new DecimalFormat();
        DECIMAL_FORMAT_PERCENTAGE.setDecimalFormatSymbols(DECIMAL_FORMAT_SYMBOLS);
    }

    private FileUtils() {}

    public static List<String> readLinesFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    public static void generateErrorResponse(String errorMessage, String filePath) throws IOException {
        writeToFile(List.of("ERROR", errorMessage), filePath);
    }

    public static void generateSuccessResponse(Check check, String filePath) throws IOException {
        List<String> resultStrings = new LinkedList<>();

        // date-time block
        resultStrings.add("Date;Time");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy;hh:mm:ss");
        resultStrings.add(check.getDateTime().format(dateTimeFormatter));

        // products block
        resultStrings.add("");
        resultStrings.add("QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL");
        check.getCheckPositions().forEach(checkPosition ->
            resultStrings.add(
                    checkPosition.getQty() + ";" +
                    checkPosition.getDescription() + ";" +
                    DECIMAL_FORMAT.format(checkPosition.getPrice()) + ";" +
                    DECIMAL_FORMAT.format(checkPosition.getDiscount()) + ";" +
                    DECIMAL_FORMAT.format(checkPosition.getTotal()))
        );

        // discount block
        if (check.getDiscountCardNum() != null){
            resultStrings.add("");
            resultStrings.add("DISCOUNT CARD;DISCOUNT PERCENTAGE");
            resultStrings.add(check.getDiscountCardNum() + ";" + DECIMAL_FORMAT_PERCENTAGE.format(check.getDiscountAmount()) + "%");
        }

        // Total price block
        resultStrings.add("");
        resultStrings.add("TOTAL PRICE;TOTAL DISCOUNT; TOTAL WITH DISCOUNT");
        resultStrings.add(
                DECIMAL_FORMAT.format(check.getTotalPrice()) + ";" +
                DECIMAL_FORMAT.format(check.getTotalDiscount()) + ";" +
                DECIMAL_FORMAT.format(check.getTotalWithDiscount()));

        writeToFile(resultStrings, filePath);
    }

    private static void writeToFile(List<String> strings, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, strings);
    }

}
