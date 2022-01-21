package com.rocketnia.hacks.jisp;

import java.util.List;
import java.util.Map;


public class SExpBuiltInCons extends SExpBuiltIn
{
    public static final SExpBuiltInCons INSTANCE = new SExpBuiltInCons();

    private SExpBuiltInCons()
    {
    }

    public SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext )
    {
        List< SExpression > args = Jisp.evaluateArgs( argument, 2, dynamicContext );

        return new SExpPair( args.get( 0 ), args.get( 1 ) );
    }
}
