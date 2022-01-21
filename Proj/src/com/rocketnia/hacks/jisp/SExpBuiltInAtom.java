package com.rocketnia.hacks.jisp;

import java.util.Map;


public class SExpBuiltInAtom extends SExpBuiltIn
{
    public static final SExpBuiltInAtom INSTANCE = new SExpBuiltInAtom();

    private SExpBuiltInAtom()
    {
    }

    public SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext )
    {
        SExpression arg = Jisp.extractArgs( argument, 1 ).get( 0 );

        return new SExpBoolean( !(arg instanceof SExpPair) );
    }
}