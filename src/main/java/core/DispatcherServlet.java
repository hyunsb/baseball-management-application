package core;

import core.annotation.RequestMapping;
import domain.Request;
import exception.BadRequestException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class DispatcherServlet {

    public static void mappingRequest(final Request request)
            throws IllegalAccessException, InvocationTargetException, BadRequestException {

        Method[] methods = SingletonPool.baseBallApp().getClass().getDeclaredMethods();

        for (Method method : methods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

            if (Objects.isNull(requestMapping)) continue;

            if (Objects.equals(requestMapping.request(), request.getHeader())) {
                method.invoke(SingletonPool.baseBallApp(), request);
                return;
            }
        }
        throw new BadRequestException("요청 형식이 올바르지 않습니다.");
    }
}
