package xbrlcore.logging;

public interface LogInterface {
    public static enum LogLevel {
        ERROR,
        WARNING,
        INFO,
        VERBOUSE,
        DEBUG,
        ;
    }

    public void setLevel(LogLevel level);

    public void log(LogLevel level, Class<?> channel, Object message);
}
