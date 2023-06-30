package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ResponseDTOPrinter {

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

    public static <T> void printResponseDTOAsPivot(T response, String colmStandard, String rowStandard, String valueStandard) {
        List<T> responses = new ArrayList<>();
        responses.add(response);
        printResponseDTOAsPivot(responses, colmStandard, rowStandard, valueStandard);
    }

    public static <T> void printResponseDTOAsPivot(List<T> responses, String colmStandard, String rowStandard, String valueStandard) {

        if (responses == null || responses.isEmpty()) {
            System.out.println("Empty Set");
            return;
        }
        printPivotTable(responses, colmStandard, rowStandard, valueStandard);
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
     * @param object field's value of responseDTO
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
     * @param valueList List of field's value of responseDTO
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
     * @param fieldNames List of field's name of responseDTO
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
     * @param values List of field's value of responseDTO
     * @param columnWidths Array of each column's width
     */
    private static void printTableRow(List<Object> values, int[] columnWidths) {
        StringBuilder rowBuilder = new StringBuilder("|");
        char space;
        for (int i = 0; i < values.size(); i++) {
            space = ' ';
            String value = values.get(i).toString();

            int padding = Math.max(0, columnWidths[i] - value.length());
            //if value contains korean replace space as unicode 2005 (quarter size space) to match alphabet letter width
            if (isContainsKorean(value)) {
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

    public static <T> void printPivotTable(List<T> responses, String colmStandard, String rowStandard, String valueStandard) {
        Map<String, Map<String, String>> pivotTableData = buildPivotTableData(responses, colmStandard, rowStandard, valueStandard);

        Map<String, Integer> columnWidths = calculateColumnWidths(pivotTableData, colmStandard);

        StringBuilder headerRow = buildHeaderRow(columnWidths, colmStandard);
        StringBuilder separatorRow = buildSeparatorRow(columnWidths, colmStandard);
        StringBuilder rows = buildRows(pivotTableData, columnWidths, colmStandard);

        StringBuilder pivotTable = new StringBuilder(headerRow.toString());
        pivotTable.append("\n").append(separatorRow).append("\n").append(rows);
        System.out.println(pivotTable);
    }

    private static <T> Map<String, Map<String, String>> buildPivotTableData(List<T> responses, String colmStandard, String rowStandard, String valueStandard) {
        Map<String, Map<String, String>> pivotTableData = new HashMap<>();

        for (T response : responses) {
            String row = getValue(response, rowStandard);
            String column = getValue(response, colmStandard);
            String value = getValue(response, valueStandard);

            pivotTableData.putIfAbsent(row, new HashMap<>());
            pivotTableData.get(row).put(column, value);
        }

        return pivotTableData;
    }

    private static <T> String getValue(T object, String fieldName) {
        try {
            Method getterMethod = object.getClass().getMethod("get" + capitalize(fieldName));
            Object value = getterMethod.invoke(object);
            return value != null ? value.toString() : "";
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private static Map<String, Integer> calculateColumnWidths(Map<String, Map<String, String>> pivotTableData, String colmStandard) {
        Map<String, Integer> columnWidths = new HashMap<>();
        columnWidths.put(colmStandard, colmStandard.length());

        for (Map<String, String> column : pivotTableData.values()) {
            for (Map.Entry<String, String> entry : column.entrySet()) {
                String columnName = entry.getKey();
                String valueName = entry.getValue();

                int columnNameLength = columnName.length();
                int valueNameLength = valueName != null ? valueName.length() : 0;

                int width = Math.max(columnWidths.getOrDefault(columnName, 0), valueNameLength);
                columnWidths.put(columnName, width);
                columnWidths.put(colmStandard, Math.max(columnWidths.get(colmStandard), columnNameLength));
            }
        }

        return columnWidths;
    }

    private static StringBuilder buildHeaderRow(Map<String, Integer> columnWidths, String colmStandard) {
        StringBuilder headerRow = new StringBuilder(formatCell(colmStandard, columnWidths.get(colmStandard)));
        for (String columnName : columnWidths.keySet()) {
            if (!columnName.equals(colmStandard)) {
                headerRow.append(" | ").append(formatCell(columnName, columnWidths.get(columnName)));
            }
        }
        return headerRow;
    }

    private static StringBuilder buildSeparatorRow(Map<String, Integer> columnWidths, String colmStandard) {
        StringBuilder separatorRow = new StringBuilder();
        separatorRow.append("-".repeat(columnWidths.get(colmStandard)));
        for (String columnName : columnWidths.keySet()) {
            if (!columnName.equals(colmStandard)) {
                separatorRow.append("-".repeat(columnWidths.get(columnName)+3));
            }
        }
        return separatorRow;
    }

    private static StringBuilder buildRows(Map<String, Map<String, String>> pivotTableData, Map<String, Integer> columnWidths, String colmStandard) {
        StringBuilder rows = new StringBuilder();
        for (String rowName : pivotTableData.keySet()) {
            StringBuilder row = new StringBuilder(formatCell(rowName, columnWidths.get(colmStandard)));
            Map<String, String> column = pivotTableData.get(rowName);

            for (String columnName : columnWidths.keySet()) {
                if (!columnName.equals(colmStandard)) {
                    String valueName = column.getOrDefault(columnName, "  ");
                    row.append("   ").append(formatCell(valueName, columnWidths.get(columnName)));
                }
            }

            rows.append(row).append("\n");
        }
        return rows;
    }

    private static String formatCell(String value, int width) {
        StringBuilder formattedValue = new StringBuilder(value);
        int padding = width - value.length();
        if (isContainsKorean(value)) {
            padding *= 0.7;
        }


        for (int i = 0; i < padding; i++) {
            formattedValue.append(" ");
        }

        return formattedValue.toString();
    }

    private static boolean isContainsKorean(String value) {
        return value.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    }
}