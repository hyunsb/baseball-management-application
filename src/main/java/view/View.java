package view;

import domain.Request;
import exception.BadRequestException;
import util.Console;
import util.RequestParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class View {

    private static final String INPUT_REQUEST = "\n어떤 기능을 요청하시겠습니까?";

    private static final String REQUEST_SUCCESS_RESULT_FORMAT = "\n[%s]\n%s\n";

    // Suppresses default constructor, ensuring non-instantiability
    private View() {
    }

    public static Request inputRequest() {
        try {
            System.out.println(INPUT_REQUEST);
            String consoleRequest = Console.readLine();
            return RequestParser.parse(consoleRequest);

        } catch (BadRequestException exception) {
            printErrorMessage(exception.getMessage());
            return inputRequest();
        }
    }

    public static <T> void printResponse(final T response) {
        ResponseDTOPrinter.printResponseDTO(response);
    }

    public static <T> void printResponse(final List<T> responses) {
        ResponseDTOPrinter.printResponseDTO(responses);
    }

    public static void printErrorMessage(String message) {
        System.out.println("\n" + message + "\n");
    }

    private static class ResponseDTOPrinter {

        private ResponseDTOPrinter() {
        }

        public static <T> void printResponseDTO(T response) {
            List<T> responses = new ArrayList<>();
            responses.add(response);
            printResponseDTO(responses);
        }

        public static <T> void printResponseDTO(List<T> responses) {

            if (responses == null || responses.isEmpty()) {
                System.out.println("Empty Set");
                return;
            }
            printAsTable(responses);
        }

        private static <T> void printAsTable(List<T> valueList) {

            Class<?> clazz = valueList.get(0).getClass();

            //Get field names
            List<String> fieldNames = getFieldNames(clazz);

            //Get maximum length of each column
            int[] columnWidths = getColumnWidths(valueList, fieldNames);

            //Print line
            printLine(columnWidths);

            //Print header(column name)
            printHeader(fieldNames, columnWidths);

            //Print line
            printLine(columnWidths);

            //Print value rows
            for (T value : valueList) {
                List<Object> fieldValues = getFieldValues(value, fieldNames);
                printTableRow(fieldValues, columnWidths);
            }

            //Print the line
            printLine(columnWidths);
        }

        /**
         * get filed name of responseDTO class
         *
         * @param clazz ResponseDTO class
         * @return List of field's name of responseDTO
         */
        private static List<String> getFieldNames(Class<?> clazz) {
            List<String> fieldNames = new ArrayList<>();
            Arrays.stream(clazz.getDeclaredFields())
                    .forEach(field -> fieldNames.add(field.getName()));
            return fieldNames;
        }

        /**
         * get value from responseDTO class's field using each field's getter
         *
         * @param object     field's value of responseDTO
         * @param fieldNames List of field's name of responseDTO
         * @return each field's value of responseDTO as List
         */
        private static List<Object> getFieldValues(Object object, List<String> fieldNames) {
            List<Object> fieldValues = new ArrayList<>();
            for (String fieldName : fieldNames) {
                try {
                    String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                    Object value = object.getClass().getMethod(getterName).invoke(object);
                    fieldValues.add(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return fieldValues;
        }

        /**
         * get each column's width
         *
         * @param valueList  List of field's value of responseDTO
         * @param fieldNames List of field's name of responseDTO
         * @return Array of each column's width
         */
        private static int[] getColumnWidths(List<?> valueList, List<String> fieldNames) {
            int[] columnWidths = new int[fieldNames.size()];
            for (int i = 0; i < fieldNames.size(); i++) {
                int maxWidth = fieldNames.get(i).length();
                for (Object object : valueList) {
                    String value = getFieldValues(object, fieldNames).get(i).toString();
                    int length = value.length();
                    int unicodeLength = value.codePointCount(0, length);
                    maxWidth = Math.max(maxWidth, unicodeLength);
                }
                columnWidths[i] = maxWidth;
            }
            return columnWidths;
        }

        /**
         * print Lines
         *
         * @param columnWidths Array of each column's width
         */
        private static void printLine(int[] columnWidths) {
            StringBuilder separator = new StringBuilder();
            for (int width : columnWidths) {
                //+3 for padding
                separator.append("-".repeat(width + 3));
            }
            separator.append("-");
            System.out.println(separator);
        }

        /**
         * Print Header
         *
         * @param fieldNames   List of field's name of responseDTO
         * @param columnWidths Array of each column's width
         */
        private static void printHeader(List<String> fieldNames, int[] columnWidths) {
            StringBuilder headerRow = new StringBuilder("|");
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                int width = columnWidths[i];
                String paddedFieldName = String.format(" %-" + width + "s |", fieldName.substring(0, Math.min(fieldName.length(), width)));
                headerRow.append(paddedFieldName);
            }
            System.out.println(headerRow);
        }

        /**
         * Print Table with value
         *
         * @param values       List of field's value of responseDTO
         * @param columnWidths Array of each column's width
         */
        private static void printTableRow(List<Object> values, int[] columnWidths) {
            StringBuilder rowBuilder = new StringBuilder("|");
            char space;
            for (int i = 0; i < values.size(); i++) {
                space = ' ';
                String value = values.get(i).toString();

                //Check if value contains Korean characters
                boolean containsKorean = value.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");

                int padding = Math.max(0, columnWidths[i] - value.length());
                //if value contains korean replace space as unicode 2005 (quarter size space) to match alphabet letter width
                if (containsKorean) {
                    space = '\u2005';
                }

                rowBuilder.append(space)
                        .append(value)
                        .append(" ".repeat(padding))
                        .append(space)
                        .append("|");
            }

            System.out.println(rowBuilder);
        }
    }
}
