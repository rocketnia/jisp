package com.rocketnia.hacks.jisp;

import java.util.Map;


public class SExpBuiltInPrint extends SExpBuiltIn
{
    public static final SExpBuiltInPrint INSTANCE = new SExpBuiltInPrint();

    private SExpBuiltInPrint()
    {
    }

    public SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext )
    {
        SExpression arg = Jisp.evaluateArgs( argument, 1, dynamicContext ).get( 0 );

        if ( arg instanceof SExpString )
        {
            System.out.println( ((SExpString)( arg )).getValue() );
        }
        else
        {
            System.out.println( arg.toString() );
        }

        return SExpEmptyList.INSTANCE;
    }
}