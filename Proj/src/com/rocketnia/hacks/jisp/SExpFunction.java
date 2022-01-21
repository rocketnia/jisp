package com.rocketnia.hacks.jisp;

import java.util.Map;


public abstract class SExpFunction implements SExpression
{
    public SExpFunction eval( Map< ? extends SExpression, ? extends SExpression > context )
    {
        return this;
    }

    public abstract SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext );
}