package xbrlcore.logging;

public class DevNullLogInterface implements LogInterface {
    public DevNullLogInterface() {
    }

    @Override
    public void setLevel(LogLevel level) {
    }

    @Override
    public void log(LogLevel level, Class<?> channel, Object message) {
    }
}
