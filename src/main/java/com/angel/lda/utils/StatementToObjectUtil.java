package com.angel.lda.utils;

import org.apache.jena.rdf.model.Statement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Riste Stojanov
 */
@SuppressWarnings("Duplicates")
public class StatementToObjectUtil {

    public static <T> Collection<T> parseList(List<Statement> statements, Class<T> type, Map<String, String> mapping) {
        Map<String, T> instances = new HashMap<>(); // Се користи за чување на објекти кои треба да ги вратиме, каде клуч ќе ни биде Subject-от од statement-от, а вредноста ќе биде објектот
        Map<String, Method> methodMapping = transformMapping(type, mapping); // Се користи за чување на методите/сеттерите за класата каде клуч ќе биде Predicate а вредност ќе биде самиот метод

        for (Statement s : statements) {

            String subject = s.getSubject().toString();
            if (!instances.containsKey(subject)) {
                try {
                    instances.put(subject, type.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            T instance = instances.get(subject);
            String predicate = s.getPredicate().toString();
            if (mapping.containsKey(predicate)) {
                Method method = methodMapping.get(predicate);

                if (method != null) {
                    try {
                        if(method.getParameterTypes()[0].equals(String.class)) {
                            method.invoke(instance, s.getObject().toString());
                        } else if(method.getParameterTypes()[0].equals(int.class)) {
                            method.invoke(instance, Integer.valueOf(s.getObject().toString()));
                        } else if(method.getParameterTypes()[0].equals(double.class)) {
                            method.invoke(instance, Double.valueOf(s.getObject().toString()));
                        } else if(method.getParameterTypes()[0].equals(Date.class)) {
                            method.invoke(instance, new SimpleDateFormat("ddMMyyyy").parse(s.getObject().toString()));
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instances.values();
    }

    private static Map<String, Method> transformMapping(Class type, Map<String, String> mapping) {

        Map<String, Method> methodMapping = new HashMap<>();

        mapping.forEach((k, v) -> {
            Method stringMethod = null;
            try{
                stringMethod = type.getDeclaredMethod(v, String.class);
            } catch (NoSuchMethodException e){}

            Method doubleMethod = null;
            try{
                doubleMethod = type.getDeclaredMethod(v, double.class);
            } catch (NoSuchMethodException e){}

            Method intMethod = null;
            try{
                intMethod = type.getDeclaredMethod(v, int.class);
            } catch (NoSuchMethodException e){}

            Method dateMethod = null;
            try{
                dateMethod = type.getDeclaredMethod(v, Date.class);
            } catch (NoSuchMethodException e) {
            }

            if(stringMethod != null){
                methodMapping.put(k, stringMethod);
            } else if(doubleMethod != null) {
                methodMapping.put(k, doubleMethod);
            } else if(intMethod != null) {
                methodMapping.put(k, intMethod);
            } else if(dateMethod != null) {
                methodMapping.put(k, dateMethod);
            }
        });
        return methodMapping;
    }
}
