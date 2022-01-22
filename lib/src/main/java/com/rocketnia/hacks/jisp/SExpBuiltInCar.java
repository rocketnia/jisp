package com.rocketnia.hacks.jisp;

import java.util.Map;


public class SExpBuiltInCar extends SExpBuiltIn
{
    public static final SExpBuiltInCar INSTANCE = new SExpBuiltInCar();

    private SExpBuiltInCar()
    {
    }

    public SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext )
    {
        SExpression arg = Jisp.evaluateArgs( argument, 1, dynamicContext ).get( 0 );

        if ( !(arg instanceof SExpPair) )
            throw new IllegalArgumentException();

        return ((SExpPair)( arg )).getFirst();
    }
}
