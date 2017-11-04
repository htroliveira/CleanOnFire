package com.cleanonfire.weaving.orm;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by heitorgianastasio on 07/10/17.
 */
@Aspect
public class RelationAspect {


    private static final String POINTCUT_METHOD =
            "execution(* * *(..))";

    private static final String POINTCUT_CONSTRUCTOR =
            "execution(* *.new(..))";

    private static final String POINTCUT_PKG =
            "within(com.cleanonfire.sample.MainActivity)";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithDebugTrace() {}

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructorAnnotatedDebugTrace() {}

    @Pointcut(POINTCUT_PKG)
    public void inPackage() {}

    @Around("inPackage()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {


        Object result = joinPoint.proceed();
        //LazyLoader.fetch(result,);
        return  result;
    }
}
