package view;

import domain.Request;
import exception.BadRequestException;
import util.Console;
import util.RequestParser;

import java.util.List;

public class View {

    private static final String INPUT_REQUEST = "어떤 기능을 요청하시겠습니까?";

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

    public static void printResponse(String request, final Object response) {
        String result = String.format(REQUEST_SUCCESS_RESULT_FORMAT, request, response);
        System.out.println(result);
    }

    public static void printResponse(String request, final List<?> responses) {
        StringBuilder stringBuilder = new StringBuilder();

        if (responses.isEmpty())
            stringBuilder.append("목록이 존재하지 않습니다.");

        responses.forEach(response -> stringBuilder.append(response).append("\n"));

        String result = String.format(REQUEST_SUCCESS_RESULT_FORMAT, request, stringBuilder);
        System.out.println(result);
    }

    public static void printErrorMessage(String message) {
        System.out.println("\n" + message + "\n");
    }
}
