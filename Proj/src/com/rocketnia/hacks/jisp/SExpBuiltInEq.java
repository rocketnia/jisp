package com.rocketnia.hacks.jisp;

import java.util.List;
import java.util.Map;


public class SExpBuiltInEq extends SExpBuiltIn
{
    public static final SExpBuiltInEq INSTANCE = new SExpBuiltInEq();

    private SExpBuiltInEq()
    {
    }

    public SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext )
    {
        List< SExpression > args = Jisp.evaluateArgs( argument, 2, dynamicContext );

        return new SExpBoolean( args.get( 0 ).equals( args.get( 1 ) ) );
    }
}