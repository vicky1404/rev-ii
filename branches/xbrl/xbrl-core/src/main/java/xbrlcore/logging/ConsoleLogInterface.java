package xbrlcore.logging;

public class ConsoleLogInterface implements LogInterface {
    private LogLevel level = LogLevel.VERBOUSE;

    public ConsoleLogInterface() {
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public void log(LogLevel level, Class<?> channel, Object message) {
        if (level.ordinal() <= this.level.ordinal()) {
            if ((level == LogLevel.WARNING) || (level == LogLevel.ERROR))
                System.err.println(message);
            else
                System.out.println(message);
        }
    }
}
