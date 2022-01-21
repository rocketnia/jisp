package com.rocketnia.hacks.jisp;

import java.util.List;
import java.util.Map;


public class SExpBuiltInIf extends SExpBuiltIn
{
    public static final SExpBuiltInIf INSTANCE = new SExpBuiltInIf();

    private SExpBuiltInIf()
    {
    }

    public SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext )
    {
        List< SExpression > args = Jisp.extractArgs( argument );
        int numberOfArgs = args.size();

        switch ( numberOfArgs )
        {
        case 2:
            args.add( SExpEmptyList.INSTANCE );
        case 3:
            SExpression condition = args.get( 0 ).eval( dynamicContext );

            if ( !(condition instanceof SExpBoolean) )
                throw new IllegalArgumentException();

            if ( ((SExpBoolean)( condition )).getValue() )
                return args.get( 1 ).eval( dynamicContext );

            return args.get( 2 ).eval( dynamicContext );
        default:
            throw new IllegalArgumentException();
        }
    }
}
