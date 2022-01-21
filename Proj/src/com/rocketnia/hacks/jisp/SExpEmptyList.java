package com.rocketnia.hacks.jisp;

import java.util.Map;


public class SExpEmptyList implements SExpression
{
    public static final SExpEmptyList INSTANCE = new SExpEmptyList();

    private SExpEmptyList()
    {
    }

    public SExpEmptyList eval( Map< ? extends SExpression, ? extends SExpression > context )
    {
        return this;
    }

    public String toString()
    {
        return "()";
    }
}