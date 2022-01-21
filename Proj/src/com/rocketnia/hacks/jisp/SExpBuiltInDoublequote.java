package com.rocketnia.hacks.jisp;

import java.util.Map;


public class SExpBuiltInDoublequote extends SExpBuiltIn
{
    public static final SExpBuiltInDoublequote INSTANCE = new SExpBuiltInDoublequote();

    private SExpBuiltInDoublequote()
    {
    }

    public SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext )
    {
        return argument;
    }
}