package core;

import core.annotation.RequestMapping;
import domain.Request;
import exception.BadRequestException;
import exception.DBConnectException;
import exception.ErrorMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class DispatcherServlet {

    public static void mappingRequest(final Request request)
            throws IllegalAccessException, InvocationTargetException, BadRequestException, DBConnectException {
        try {
            Method[] methods = SingletonPool.baseBallApp().getClass().getDeclaredMethods();

            for (Method method : methods) {
                RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

                if (Objects.isNull(requestMapping)) continue;

                if (Objects.equals(requestMapping.request(), request.getHeader())) {
                    method.invoke(SingletonPool.baseBallApp(), request);
                    return;
                }
            }
        } catch (ExceptionInInitializerError exception) {
            throw new DBConnectException(ErrorMessage.INVALID_DB_CONNECTION);
        }
        throw new BadRequestException();
    }
}
