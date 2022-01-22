package com.rocketnia.hacks.jisp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;


public class Jisp
{
    public static void main( String[] args ) throws Exception
    {
        Set< SExpression > defaultStringTakers = new HashSet< SExpression >();
        defaultStringTakers.add( SExpSymbol.valueOf( "doublequote" ) );

        HashMap< Character, SExpression > defaultAbbreviations = new HashMap< Character, SExpression >();
        defaultAbbreviations.put( Character.valueOf( '?' ), SExpSymbol.valueOf( "print" ) );
        defaultAbbreviations.put( Character.valueOf( '\'' ), SExpSymbol.valueOf( "quote" ) );
        defaultAbbreviations.put( Character.valueOf( '\"' ), SExpSymbol.valueOf( "doublequote" ) );

        HashMap< SExpression, SExpression > defaultContext = new HashMap< SExpression, SExpression >();
        defaultContext.put( SExpSymbol.valueOf( "exit"  ),        SExpBuiltInExit.INSTANCE  );
        defaultContext.put( SExpSymbol.valueOf( "print" ),        SExpBuiltInPrint.INSTANCE );
        defaultContext.put( SExpSymbol.valueOf( "quote" ),        SExpBuiltInQuote.INSTANCE );
        defaultContext.put( SExpSymbol.valueOf( "doublequote" ),  SExpBuiltInDoublequote.INSTANCE );
        defaultContext.put( SExpSymbol.valueOf( "atom" ),         SExpBuiltInAtom.INSTANCE );
        defaultContext.put( SExpSymbol.valueOf( "eq" ),           SExpBuiltInEq.INSTANCE );
        defaultContext.put( SExpSymbol.valueOf( "cons" ),         SExpBuiltInCons.INSTANCE );
        defaultContext.put( SExpSymbol.valueOf( "car" ),          SExpBuiltInCar.INSTANCE );
        defaultContext.put( SExpSymbol.valueOf( "cdr" ),          SExpBuiltInCdr.INSTANCE );
        defaultContext.put( SExpSymbol.valueOf( "if" ),           SExpBuiltInIf.INSTANCE );

        // Set up the console input.
        BufferedReader input = new BufferedReader( new InputStreamReader( System.in ) );

        while ( true )
        {
            String code;

            System.out.println();
            System.out.print( "> " );
            try
            {
                code = input.readLine();
            }
            catch ( IOException e )
            {
                System.out.println();
                System.out.println( "input error:" );
                System.out.flush();
                e.printStackTrace( System.out );
                continue;
            }

            System.out.println();

            SExpression result;
            try
            {
                result = evalElement( code, defaultStringTakers, defaultAbbreviations, defaultContext );
            }
            catch( JispParseException e )
            {
                System.out.println();
                System.out.println( "parse error:" );
                System.out.flush();
                e.printStackTrace( System.out );
                continue;
            }
            catch( NotAFunctionException e )
            {
                System.out.println();
                System.out.println( "nonfunction error (" + e.getPurportedFunction() + "):" );
                System.out.flush();
                e.printStackTrace( System.out );
                continue;
            }
            catch( IllegalArgumentException e )
            {
                System.out.println();
                System.out.println( "argument error:" );
                System.out.flush();
                e.printStackTrace( System.out );
                continue;
            }

            System.out.println();
            System.out.println( "result: " + result );
        }
    }

    public static SExpression evalElement( String source, Set< SExpression > stringTakers, Map< Character, SExpression > abbreviations, Map< SExpression, SExpression > context ) throws JispParseException, NotAFunctionException
    {
        return parseElement( source, stringTakers, abbreviations ).eval( context );
    }

    private static int getElementEnd( String source, int startOffset ) throws JispParseException
    {
        int sourceLength = source.length();
        int nestingDepth = 0;
        boolean includesParentheses = false;
        for ( int midOffset = startOffset; midOffset < sourceLength; midOffset++ )
        {
            if ( nestingDepth == 0 )
            {
                if ( includesParentheses )
                    return midOffset;

                char currentChar = source.charAt( midOffset );

                if ( (currentChar == ')') || Character.isWhitespace( currentChar ) )
                    return midOffset;
            }

            switch ( source.charAt( midOffset ) )
            {
            case '(':
                nestingDepth++;
                includesParentheses = true;
                break;
            case ')':
                nestingDepth--;
                break;
            default:
                break;
            }
        }

        if ( nestingDepth != 0 )
            throw new JispParseException();

        return sourceLength;
    }

    public static SExpression parseList( String source, Set< SExpression > stringTakers, Map< Character, SExpression > abbreviations ) throws JispParseException
    {
        // Strip the whitespace off of the ends.
        int startOffset = 0;
        int endOffset = source.length();
        for ( ; (startOffset < endOffset) && Character.isWhitespace( source.charAt( startOffset ) ); startOffset++ );
        endOffset--;
        for ( ; (startOffset < endOffset) && Character.isWhitespace( source.charAt( endOffset ) ); endOffset-- );
        endOffset++;

        // If the string is empty, this is the empty list.
        if ( startOffset == endOffset )
            return SExpEmptyList.INSTANCE;

        // Parse the first element of the list.
        int midOffset = getElementEnd( source, startOffset );
        SExpression first = parseElement( source.substring( startOffset, midOffset ), stringTakers, abbreviations );

        // If the first element is a string taker...
        if ( stringTakers.contains( first ) )
        {
            // Pair it with a string containing its arguments with no whitespace at the beginning.
            for ( ; (midOffset < endOffset) && Character.isWhitespace( source.charAt( midOffset ) ); midOffset++ );
            return new SExpPair( first, new SExpString( source.substring( midOffset, endOffset ) ) );
        }

        // Otherwise, parse its arguments before constructing the list.
        return new SExpPair( first, parseList( source.substring( midOffset, endOffset ), stringTakers, abbreviations ) );
    }

    public static SExpression parseElement( String source, Set< SExpression > stringTakers, Map< Character, SExpression > abbreviations ) throws JispParseException
    {
        // Strip the whitespace off of the ends.
        int startOffset = 0;
        int endOffset = source.length();
        for ( ; (startOffset < endOffset) && Character.isWhitespace( source.charAt( startOffset ) ); startOffset++ );
        endOffset--;
        for ( ; (startOffset < endOffset) && Character.isWhitespace( source.charAt( endOffset ) ); endOffset-- );
        endOffset++;

        // Make sure that this is exactly one element.
        if ( (startOffset == endOffset) || (endOffset != getElementEnd( source, startOffset )) )
            throw new JispParseException();

        // If there is now a parenthesis...
        char currentChar = source.charAt( startOffset );
        if ( currentChar == '(' )
        {
            // Strip the parentheses off of the ends.
            startOffset++;
            endOffset--;

            // Parse the list.
            return parseList( source.substring( startOffset, endOffset ), stringTakers, abbreviations );
        }
        else  // Otherwise...
        {
            // If there is now an abbreviation...
            Character currentCharacter = Character.valueOf( currentChar );
            if ( abbreviations.containsKey( currentCharacter ) )
            {
                // Make sure that this isn't the end of the string.
                if ( endOffset == startOffset + 1 )
                    throw new JispParseException();

                // Expand the abbreviation.
                SExpression first = abbreviations.get( currentCharacter );

                // If the abbreviation stands for a string taker...
                if ( stringTakers.contains( first ) )
                {
                    // Pair it with a string containing its argument.
                    return new SExpPair( first, new SExpString( source.substring( startOffset + 1, endOffset ) ) );
                }

                // Otherwise, parse its argument before pairing the two.
                return new SExpPair( first, new SExpPair( parseElement( source.substring( startOffset + 1, endOffset ), stringTakers, abbreviations ), SExpEmptyList.INSTANCE ) );
            }
            else  // Otherwise...
            {
                // Parse the symbol.
                return SExpSymbol.valueOf( source.substring( startOffset, endOffset ) );
            }
        }
    }

    public static List< SExpression > extractArgs( SExpression argList, int numberOfArgs )
    {
        List< SExpression > args = new ArrayList< SExpression >( numberOfArgs );

        if ( argList instanceof SExpEmptyList )
        {
            if ( numberOfArgs != 0 )
                throw new IllegalArgumentException();

            return args;
        }

        if ( !(argList instanceof SExpPair) )
            throw new IllegalArgumentException();

        args.add( ((SExpPair)( argList )).getFirst() );
        args.addAll( extractArgs( ((SExpPair)( argList )).getSecond(), numberOfArgs - 1 ) );

        return args;
    }

    public static List< SExpression > extractArgs( SExpression argList )
    {
        List< SExpression > args = new ArrayList< SExpression >();

        if ( argList instanceof SExpEmptyList )
            return args;

        if ( !(argList instanceof SExpPair) )
            throw new IllegalArgumentException();

        args.add( ((SExpPair)( argList )).getFirst() );
        args.addAll( extractArgs( ((SExpPair)( argList )).getSecond() ) );

        return args;
    }

    public static List< SExpression > evaluateArgs( SExpression argList, int numberOfArgs, Map< ? extends SExpression, ? extends SExpression > context )
    {
        List< SExpression > args = extractArgs( argList, numberOfArgs );

        for ( int thisArg = 0; thisArg < numberOfArgs; thisArg++ )
        {
            args.set( thisArg, args.get( thisArg ).eval( context ) );
        }

        return args;
    }
}
