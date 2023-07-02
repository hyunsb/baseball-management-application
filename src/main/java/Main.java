import core.DispatcherServlet;
import domain.Request;
import exception.BadRequestException;
import exception.RollbackException;
import exception.ServiceFailureException;
import view.View;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) {
        while (true) {
            try {
                Request request = View.inputRequest();
                DispatcherServlet.mappingRequest(request);
            } catch (BadRequestException | ServiceFailureException exception) {
                View.printErrorMessage(exception.getMessage());
            } catch (RollbackException | IllegalAccessException | InvocationTargetException exception) {
                View.printResponse(exception.getCause().getCause());
            }
        }
        // Console.close(); 프로그램 종료 시 InputStream close
        // SingletonPool.ConnectionPoolManager().closeAllConnections(); 프로그램 종료 시 모든 커넥션 close
    }
}
