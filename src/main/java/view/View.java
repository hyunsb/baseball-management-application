package view;

import domain.Request;
import util.Console;
import util.RequestParser;

public class View {

    private static final String INPUT_REQUEST = "어떤 기능을 요청하시겠습니까?";

    // Suppresses default constructor, ensuring non-instantiability
    private View() {
    }

    public static Request inputRequest() {
        System.out.println(INPUT_REQUEST);
        String consoleRequest = Console.readLine();
        return RequestParser.parse(consoleRequest);
    }
}
