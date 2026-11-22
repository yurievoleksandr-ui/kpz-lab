import annotations.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ComponentEngine {

    public static void run(String packageName) throws Exception {
        List<Class<?>> allClasses = scan(packageName);
        List<Class<?>> components = new ArrayList<>();
        List<Class<?>> skipped = new ArrayList<>();

        for (Class<?> c : allClasses) {
            if (c.isAnnotationPresent(Component.class)) {
                components.add(c);
            } else {
                skipped.add(c);
            }
        }

        System.out.println("=== Сканування пакета '" + packageName + "' ===");
        System.out.println("Знайдено класів: " + allClasses.size()
                + ", з @Component: " + components.size());
        skipped.forEach(c -> System.out.println("  пропущено (без @Component): " + c.getSimpleName()));
        System.out.println();

        for (Class<?> clazz : components) {
            Component meta = clazz.getAnnotation(Component.class);
            System.out.println("--- " + clazz.getSimpleName()
                    + " (name=" + meta.name() + ", version=" + meta.version() + ") ---");

            // 1. Динамічне створення екземпляра
            Object instance = clazz.getDeclaredConstructor().newInstance();

            // 2. Динамічне встановлення значення поля
            Field field = clazz.getDeclaredFields()[0];
            field.setAccessible(true);
            field.set(instance, meta.name() + "-v" + meta.version());

            // 3. Динамічний виклик методу дії
            Method action = clazz.getDeclaredMethod("execute");
            action.invoke(instance);

            // 4. Підсумковий стан через toString()
            System.out.println("Стан: " + instance);
            System.out.println();
        }
    }

    private static List<Class<?>> scan(String packageName) throws Exception {
        List<Class<?>> result = new ArrayList<>();
        String path = packageName.replace('.', '/');
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = loader.getResources(path);

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            if ("file".equals(url.getProtocol())) {
                File dir = new File(url.toURI());
                File[] files = dir.listFiles((d, name) -> name.endsWith(".class"));
                if (files != null) {
                    for (File f : files) {
                        String simple = f.getName().replace(".class", "");
                        result.add(Class.forName(packageName + "." + simple));
                    }
                }
            } else if ("jar".equals(url.getProtocol())) {
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(path + "/") && name.endsWith(".class")) {
                        String className = name.replace('/', '.').replace(".class", "");
                        result.add(Class.forName(className));
                    }
                }
            }
        }
        result.sort(Comparator.comparing(Class::getSimpleName));
        return result;
    }
}