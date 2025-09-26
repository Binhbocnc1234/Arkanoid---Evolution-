package framework;

// Generic singleton base class
public abstract class Singleton<T> {
    private static Object instance;

    protected Singleton() {}

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
