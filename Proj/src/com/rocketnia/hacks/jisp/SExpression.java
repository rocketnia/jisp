package com.rocketnia.hacks.jisp;

import java.util.Map;


public interface SExpression
{
    SExpression eval( Map< ? extends SExpression, ? extends SExpression > context );
}