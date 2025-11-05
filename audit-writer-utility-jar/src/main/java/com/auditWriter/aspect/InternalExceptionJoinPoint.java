package com.auditWriter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;

public class InternalExceptionJoinPoint implements ProceedingJoinPoint {

    Exception exception;

    public InternalExceptionJoinPoint(Exception exception) {
        this.exception =exception;
    }

    @Override
    public void set$AroundClosure(AroundClosure aroundClosure) {

    }

    @Override
    public Object proceed() throws Throwable {
        return null;
    }

    @Override
    public Object proceed(Object[] objects) throws Throwable {
        return null;
    }

    @Override
    public String toShortString() {
        return null;
    }

    @Override
    public String toLongString() {
        return exception.getClass().toString();
    }

    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public Object getTarget() {
        return null;
    }

    @Override
    public Object[] getArgs() {
        return new String[]{exception.getLocalizedMessage()};
    }

    @Override
    public Signature getSignature() {
        return null;
    }

    @Override
    public SourceLocation getSourceLocation() {
        return null;
    }

    @Override
    public String getKind() {
        return null;
    }

    @Override
    public StaticPart getStaticPart() {
        return null;
    }


}
