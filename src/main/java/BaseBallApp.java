import annotation.RequestMapping;
import dao.StadiumDAO;
import db.DBConnection;
import domain.Request;
import service.StadiumService;
import view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class BaseBallApp {

    private static final StadiumService stadiumService;

    static {
        stadiumService = new StadiumService(new StadiumDAO(DBConnection.getInstance()));
    }

    public static void main(String[] args) {
        while (true) {
            try {
                Request request = View.inputRequest();
                mappingRequest(request);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                System.out.println("Reflection Error");
            }
        }
    }

    public static void mappingRequest(final Request request) throws IllegalAccessException, InvocationTargetException {
        Method[] methods = BaseBallApp.class.getDeclaredMethods();

        for (Method method : methods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

            if (Objects.isNull(requestMapping)) continue;

            if (Objects.equals(requestMapping.request(), request.getHeader())) {
                method.invoke(BaseBallApp.class, request);
            }
        }
    }
}
