package com.rocketnia.hacks.jisp;

import java.util.Map;


public class SExpBuiltInQuote extends SExpBuiltIn
{
    public static final SExpBuiltInQuote INSTANCE = new SExpBuiltInQuote();

    private SExpBuiltInQuote()
    {
    }

    public SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext )
    {
        return Jisp.extractArgs( argument, 1 ).get( 0 );
    }
}