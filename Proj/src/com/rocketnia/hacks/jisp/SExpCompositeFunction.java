package com.rocketnia.hacks.jisp;

import java.util.HashMap;
import java.util.Map;


// TODO: Make heads or tails of the following comments.

// the argument names here are deep-modifiable using what I consider to be a quite clever but potentially confusing means
// replaceArgumentName tries to replace all instances of the old argument name with the new one, and if there's another function somewhere along the line that uses the new one, it has that function use the old one instead (disregarding whether that makes sense)
// this makes it so that argument names are all rotated up one level until the old one can finally be put on the bottom level
// come to think of it, this it isn't entirely useless to keep track of the argument names; non-built-in functions should probably work exactly like this
// as such, this should probably be, in some form, a subclass of the "actual" SExpFunction class

// also, one question: should the non-argument context be taken from when the function is defined or when the function is used?
// it seems to me like it should be taken from when the function is defined so that people can't do unexpected things to it when they re-use the symbols used in it in another place


public class SExpCompositeFunction extends SExpFunction
{
    private SExpression argumentName;
    private SExpression body;
    private Map< SExpression, SExpression > definitionContext;

    public SExpCompositeFunction( SExpression wantedArgumentName, SExpression wantedBody, Map< SExpression, SExpression > wantedDefinitionContext )
    {
        argumentName = wantedArgumentName;
        body = wantedBody;
        definitionContext = wantedDefinitionContext;
    }

    public SExpression getArgumentName()
    {
        return argumentName;
    }

    public SExpression getBody()
    {
        return body;
    }

    public Map< SExpression, SExpression > getDefinitionContext()
    {
        return definitionContext;
    }

    public void setArgumentName( SExpression wantedArgumentName )
    {
        argumentName = wantedArgumentName;
    }

    public void setBody( SExpression wantedBody )
    {
        body = wantedBody;
    }

    public void setDefinitionContext( Map< SExpression, SExpression > wantedDefinitionContext )
    {
        definitionContext = wantedDefinitionContext;
    }

    public void replaceArgumentName( SExpression wantedArgumentName )
    {
        if ( body == argumentName )
        {
            body = wantedArgumentName;
        }
        else if ( body == wantedArgumentName )
        {
            body = argumentName;
        }
        else
        {
            replaceArgumentNameIn( body, wantedArgumentName );
        }

        if ( definitionContext.containsKey( wantedArgumentName ) )
        {
            definitionContext.put( argumentName, definitionContext.remove( wantedArgumentName ) );
        }

        argumentName = wantedArgumentName;
    }

    private void replaceArgumentNameIn( SExpPair environment, SExpression wantedArgumentName )
    {
        SExpression first = environment.getFirst();

        if ( first == argumentName )
        {
            environment.setFirst( wantedArgumentName );
        }
        else if ( first == wantedArgumentName )
        {
            environment.setFirst( argumentName );
        }
        else
        {
            replaceArgumentNameIn( first, wantedArgumentName );
        }

        SExpression second = environment.getSecond();

        if ( second == argumentName )
        {
            environment.setSecond( wantedArgumentName );
        }
        else if ( second == wantedArgumentName )
        {
            environment.setSecond( argumentName );
        }
        else
        {
            replaceArgumentNameIn( second, wantedArgumentName );
        }
    }

    private void replaceArgumentNameIn( SExpCompositeFunction environment, SExpression wantedArgumentName )
    {
        SExpression innerArgumentName = environment.getArgumentName();

        if ( innerArgumentName == argumentName )
        {
            environment.replaceArgumentName( wantedArgumentName );
        }
        else if ( innerArgumentName == wantedArgumentName )
        {
            environment.replaceArgumentName( argumentName );
        }
        else
        {
            replaceArgumentNameIn( environment.getBody(), wantedArgumentName );


            // not sure whether all of this is strictly necessary or even desired, so it needs some testing

            if ( environment.definitionContext.containsKey( wantedArgumentName ) )
            {
                if ( environment.definitionContext.containsKey( argumentName ) )
                {
                    SExpression wantedArgumentNameValue = environment.definitionContext.remove( wantedArgumentName );
                    SExpression       argumentNameValue = environment.definitionContext.remove(       argumentName );

                    environment.definitionContext.put(       argumentName, wantedArgumentNameValue );
                    environment.definitionContext.put( wantedArgumentName,       argumentNameValue );
                }
                else
                {
                    environment.definitionContext.put(       argumentName, definitionContext.remove( wantedArgumentName ) );
                }
            }
            else
            {
                if ( environment.definitionContext.containsKey( argumentName ) )
                {
                    environment.definitionContext.put( wantedArgumentName, definitionContext.remove(       argumentName ) );
                }
            }
        }
    }

    private void replaceArgumentNameIn( SExpression environment, SExpression wantedArgumentName )
    {
        if ( environment instanceof SExpPair               )
        {
            replaceArgumentNameIn( (SExpPair               )( environment ), wantedArgumentName );
            return;
        }

        if ( environment instanceof SExpCompositeFunction  )
        {
            replaceArgumentNameIn( (SExpCompositeFunction  )( environment ), wantedArgumentName );
            return;
        }
    }

    public SExpression applyTo( SExpression argument, Map< ? extends SExpression, ? extends SExpression > dynamicContext )
    {
        Map< SExpression, SExpression > innerApplicationContext = new HashMap< SExpression, SExpression >( definitionContext );
        innerApplicationContext.put( argumentName, argument );
        return body.eval( innerApplicationContext );
    }
}