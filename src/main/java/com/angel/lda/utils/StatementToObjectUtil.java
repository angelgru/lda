package com.angel.lda.utils;

import org.apache.jena.rdf.model.Statement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Riste Stojanov
 */
public class StatementToObjectUtil {

  public static <T> Collection<T> parseList(List<Statement> statements, Class<T> type, Map<String, String> mapping) throws IllegalAccessException, InstantiationException, InvocationTargetException {
    Map<String, T> instances = new HashMap<>();
    Map<String, Method> methodMapping = transformMapping(type, mapping);

    Method[] methods = type.getDeclaredMethods();
    for (Statement s : statements) {
      String subject = s.getSubject().toString();
      if (!instances.containsKey(subject)) {
        instances.put(subject, type.newInstance());
      }
      T instance = instances.get(subject);
      String predicate = s.getPredicate().toString();
      if (mapping.containsKey(predicate)) {
        Method method = methodMapping.get(predicate);

        if (method != null) {
          method.invoke(instance, s.getObject().toString());
        }

      } else {
        // go ignorirame
      }
    }
    return instances.values();
  }

  public static Map<String, Method> transformMapping(Class type, Map<String, String> mapping) {
    return null;
  }


}
