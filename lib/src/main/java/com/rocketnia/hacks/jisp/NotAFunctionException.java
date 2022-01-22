package com.rocketnia.hacks.jisp;


public class NotAFunctionException extends Exception
{
    static final long serialVersionUID = 0;

    String purportedFunction;

    public NotAFunctionException( String assignedPurportedFunction )
    {
        purportedFunction = assignedPurportedFunction;
    }

    public String getPurportedFunction()
    {
        return purportedFunction;
    }
}
