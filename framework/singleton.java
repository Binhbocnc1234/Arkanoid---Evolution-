package framework;

// Generic singleton base class
public abstract class singleton<T> {
    private static Object instance;

    protected singleton() {}

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz) {
        if (instance == null) {
            try {
                instance = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Cannot create singleton instance", e);
            }
        }
        return (T) instance;
    }
}
