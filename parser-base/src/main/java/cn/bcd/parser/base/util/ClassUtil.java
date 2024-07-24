package cn.bcd.parser.base.util;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ClassUtil {

    /**
     * 根据父类找出所有子类,去除接口和抽象类
     *
     * @param parentClass
     * @param packageNames
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getClassesByParentClass(Class<?> parentClass, String... packageNames) throws IOException, ClassNotFoundException {
        Set<Class<?>> classSet = new HashSet<>();
        for (String packageName : packageNames) {
            classSet.addAll(getClasses(packageName));
        }
        return classSet.stream().filter(e -> {
            int modifiers = e.getModifiers();
            return !Modifier.isInterface(modifiers) && !Modifier.isAbstract(modifiers) && parentClass.isAssignableFrom(e);
        }).collect(Collectors.toList());
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param packageName
     * @return
     */
    public static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        // 第一个class类的集合
        List<Class<?>> classes = new ArrayList<>();
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        // 循环迭代下去
        while (dirs.hasMoreElements()) {
            // 获取下一个元素
            URL url = dirs.nextElement();
            // 得到协议的名称
            String protocol = url.getProtocol();
            // 如果是以文件的形式保存在服务器上
            if ("file".equals(protocol)) {
                // 获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
                // 以文件的方式扫描整个包下的文件 并添加到集合中
                findAndAddClassesInPackageByFile(packageName, filePath, classes);
            } else if ("jar".equals(protocol)) {
                // 如果是jar包文件
                // 定义一个JarFile
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                // 从此jar包 得到一个枚举类
                Enumeration<JarEntry> entries = jar.entries();
                // 同样的进行循环迭代
                while (entries.hasMoreElements()) {
                    // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    // 如果是以/开头的
                    if (name.charAt(0) == '/') {
                        // 获取后面的字符串
                        name = name.substring(1);
                    }
                    // 如果前半部分和定义的包名相同
                    if (name.startsWith(packageDirName)) {
                        int idx = name.lastIndexOf('/');
                        // 如果以"/"结尾 是一个包
                        if (idx != -1) {
                            // 获取包名 把"/"替换成"."
                            packageName = name.substring(0, idx).replace('/', '.');
                        }
                        // 如果可以迭代下去 并且是一个包
                        // 如果是一个.class文件 而且不是目录
                        if (name.endsWith(".class") && !entry.isDirectory()) {
                            // 去掉后面的".class" 获取真正的类名
                            String className = name.substring(packageName.length() + 1, name.length() - 6);
                            // 添加到classes
                            classes.add(Class.forName(packageName + '.' + className));
                        }
                    }
                }
            }
        }
        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param classes
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, List<Class<?>> classes) throws ClassNotFoundException {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
        File[] dirfiles = dir.listFiles(file -> file.isDirectory() || (file.getName().endsWith(".class")));
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                // 添加到集合中去
                classes.add(Class.forName(packageName + '.' + className));

            }
        }
    }


    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        try {
            final Field field = clazz.getField("TYPE");
            field.setAccessible(true);
            return ((Class<?>) field.get(null)).isPrimitive();
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException ex) {
            return false;
        }
    }


    /**
     * 获取class所有字段、会获取其父类字段
     * public、非static
     *
     * @param clazz
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        final List<Class<?>> classList = new ArrayList<>();
        classList.add(clazz);
        Class<?> temp = clazz;
        while (true) {
            temp = temp.getSuperclass();
            if (temp == null || Object.class == temp) {
                break;
            } else {
                classList.add(0, temp);
            }
        }
        final List<Field> resList = new ArrayList<>();
        for (Class<?> c : classList) {
            //过滤掉 final、static关键字修饰、且不是public的字段
            final List<Field> fieldList = Arrays.stream(c.getDeclaredFields())
                    .filter(e -> !Modifier.isStatic(e.getModifiers()) &&
                            Modifier.isPublic(e.getModifiers())).toList();
            resList.addAll(fieldList);
        }
        return resList;

    }
}
