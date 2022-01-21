package com.rocketnia.hacks.jisp;

import java.util.Map;


public class SExpString implements SExpression
{
    private StringBuilder value;

    public SExpString( Object wantedValue )
    {
        setValue( wantedValue );
    }

    public StringBuilder getValue()
    {
        return value;
    }

    public void setValue( Object wantedValue )
    {
        if ( wantedValue instanceof StringBuilder )
        {
            value = (StringBuilder)( wantedValue );
            return;
        }

        if ( wantedValue instanceof CharSequence )
        {
            value = new StringBuilder( (CharSequence)( wantedValue ) );
            return;
        }

        value = new StringBuilder( wantedValue.toString() );
    }

    public SExpString eval( Map< ? extends SExpression, ? extends SExpression > context )
    {
        return this;
    }

    public String toString()
    {
        return "\"" + value + "\"";
    }

    public boolean equals( Object other )
    {
        return ( (other != null) && (other instanceof SExpString) && value.toString().equals( ((SExpString)( other )).value.toString() ) );
    }
}