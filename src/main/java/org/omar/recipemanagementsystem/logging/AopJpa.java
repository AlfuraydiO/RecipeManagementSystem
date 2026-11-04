package org.omar.recipemanagementsystem.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collection;

@Component
@Aspect
public class AopJpa {
  private static final Logger log = LoggerFactory.getLogger(AopJpa.class);

  /**
   * Matches every Spring Data repository bean in the application.
   *
   * <p>"within(Type+)" means: the type of the bean must be the given type OR a subtype of it,
   * so every bean whose interface extends a "*Repository" interface is matched.
   *
   * <p>IMPORTANT: "target(...)" and "within(...)" accept a TYPE name only - no wildcards in
   * "target(...)" and no method signature. Writing "target(...*Repository.*())" produces an
   * invalid AspectJ expression; Spring parses it lazily, catches the parse error and only logs
   * it at DEBUG level ("Pointcut parser rejected expression [...]"), so the advisor is silently
   * never applied. Use "within(...*Repository+)" or "execution(* ...*Repository.*(..))" instead.
   */
  @Pointcut("within(org.omar.recipemanagementsystem..*Repository+)")
  public void repositoryMethods() {}

  @Before("repositoryMethods()")
  public void logMethodCall(JoinPoint jp) {
  }

  @AfterReturning(pointcut = "repositoryMethods()", returning = "result")
  public void logResult(JoinPoint jp, Object result) {

    log.info("result of {} {}: args {} : {}",jp.getSignature().getDeclaringType().getSimpleName(), jp.getSignature().getName(), Arrays.stream(jp.getArgs()).toList().stream().map(e->e.toString()).toList(), summarize(result));
  }


  private String summarize(Object result) {
    if (result == null) {
      return "null";
    }
    if (result instanceof Collection<?> collection) {
      return collection.getClass().getSimpleName() + "(size=" + collection.size() + ")";
    }
    if (result instanceof Iterable<?> iterable) {
      int size = 0;
      for (Object ignored : iterable) {
        size++;
      }
      return result.getClass().getSimpleName() + "(size=" + size + ")";
    }
    if (result instanceof Object[] array) {
      return result.getClass().getSimpleName() + "(length=" + array.length + ")";
    }
    return result.getClass().getSimpleName() + ": " + result;
  }
}
