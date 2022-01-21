package com.rocketnia.hacks.jisp;

import java.util.Map;


public class SExpBuiltInExit extends SExpBuiltIn
{
    public static final SExpBuiltInExit INSTANCE = new SExpBuiltInExit();

    private SExpBuiltInExit()
    {
    }

    public SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext )
    {
        System.exit( 0 );
        return null;
    }
}