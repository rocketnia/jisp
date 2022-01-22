package com.rocketnia.hacks.jisp;

import java.util.Map;


public class SExpPair implements SExpression
{
    private SExpression first;
    private SExpression second;

    public SExpPair( SExpression wantedFirst, SExpression wantedSecond )
    {
        if ( (wantedFirst == null) || (wantedSecond == null) )
            throw new NullPointerException();

        first = wantedFirst;
        second = wantedSecond;
    }

    public SExpression getFirst()
    {
        return first;
    }

    public SExpression getSecond()
    {
        return second;
    }

    public void setFirst( SExpression wantedFirst )
    {
        first = wantedFirst;
    }

    public void setSecond( SExpression wantedSecond )
    {
        second = wantedSecond;
    }

    public boolean isList()
    {
        return testList( second );
    }

    private static boolean testList( SExpression list )
    {
        if ( list instanceof SExpPair )
            return testList( ((SExpPair)( list )).second );

        if ( list instanceof SExpEmptyList )
            return true;

        return false;
    }

    public SExpression eval( Map< ? extends SExpression, ? extends SExpression > context )
    {
        SExpression firstValue = first.eval( context );

        if ( firstValue instanceof SExpFunction )
            return ((SExpFunction)( firstValue )).applyTo( second, context );

        return new SExpPair( firstValue, second );
    }

    public String toString()
    {
        if ( testList( second ) )
            return "(" + intermediateToString( this ) + ")";

        return "(" + first.toString() + " . " + second.toString() + ")";
    }

    private static String intermediateToString( SExpression list )
    {
        // assumes that the given SExpression is a list

        if ( !(list instanceof SExpPair) )
            return "";

        String rest = intermediateToString( ((SExpPair)( list )).second );

        if ( rest == "" )
            return ((SExpPair)( list )).first.toString();

        return ( ((SExpPair)( list )).first.toString() + " " + rest );
    }

    public boolean equals( Object other )
    {
        return ( (other != null) && (other instanceof SExpPair) && first.equals( ((SExpPair)( other )).first ) && second.equals( ((SExpPair)( other )).second ) );
    }
}
