package fr.leblanc.gomoku.exception;

public class EngineException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public EngineException(final String message) {
        super(message);
    }
}