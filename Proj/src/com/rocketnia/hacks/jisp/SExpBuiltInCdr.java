package com.rocketnia.hacks.jisp;

import java.util.Map;


public class SExpBuiltInCdr extends SExpBuiltIn
{
    public static final SExpBuiltInCdr INSTANCE = new SExpBuiltInCdr();

    private SExpBuiltInCdr()
    {
    }

    public SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext )
    {
        SExpression arg = Jisp.evaluateArgs( argument, 1, dynamicContext ).get( 0 );

        if ( !(arg instanceof SExpPair) )
            throw new IllegalArgumentException();

        return ((SExpPair)( arg )).getSecond();
    }
}
