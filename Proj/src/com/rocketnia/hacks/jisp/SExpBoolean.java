package com.rocketnia.hacks.jisp;

import java.util.Map;


public class SExpBoolean implements SExpression
{
    private boolean value;

    public SExpBoolean( boolean wantedValue )
    {
        value = wantedValue;
    }

    public boolean getValue()
    {
        return value;
    }

    public void setValue( boolean wantedValue )
    {
        value = wantedValue;
    }

    public SExpBoolean eval( Map< ? extends SExpression, ? extends SExpression > context )
    {
        return this;
    }

    public String toString()
    {
        if ( value )
            return "#t";

        return "#f";
    }

    public boolean equals( Object other )
    {
        return ( (other != null) && (other instanceof SExpBoolean) && (value == ((SExpBoolean)( other )).value) );
    }
}