package com.rocketnia.hacks.jisp;

import java.util.Map;
import java.util.HashMap;


public class SExpSymbol implements SExpression
{
    private static HashMap< String, SExpSymbol > instances  = new HashMap< String, SExpSymbol >();
    private static HashMap< SExpSymbol, String > names      = new HashMap< SExpSymbol, String >();

    public static SExpSymbol valueOf( String name )
    {
        if ( instances.containsKey( name ) )
            return instances.get( name );

        SExpSymbol value = new SExpSymbol();
        instances.put( name, value );
        names.put( value, name );
        return value;
    }

    public static SExpSymbol valueOf( Object name )
    {
        return valueOf( name.toString() );
    }

    public String getName()
    {
        return names.get( this );
    }

    public void setName( String name )
    {
        instances.remove( names.get( this ) );
        names.put( this, name );
        instances.put( name, this );
    }

    public void setName( Object name )
    {
        setName( name.toString() );
    }

    public SExpression eval( Map< ? extends SExpression, ? extends SExpression > context )
    {
        SExpression value = context.get( this );

        if ( value == null )
            return SExpEmptyList.INSTANCE;

        return value;
    }

    public String toString()
    {
        return names.get( this );
    }
}
