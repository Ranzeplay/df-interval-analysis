package seminar.dataflow.util.taie;

import pascal.taie.Main;
import pascal.taie.World;
import pascal.taie.analysis.graph.cfg.CFG;
import pascal.taie.analysis.graph.cfg.CFGBuilder;
import pascal.taie.config.AnalysisConfig;
import pascal.taie.ir.IR;
import pascal.taie.ir.stmt.Stmt;
import pascal.taie.language.classes.JClass;
import pascal.taie.language.classes.JMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper to run analyses with Tai-e: it loads a target class and gives access to the
 * intermediate representation (IR) and the control-flow graph (CFG) of its methods.
 */
public final class TaieRunner {

    private TaieRunner() {
    }

    /**
     * Builds Tai-e's model of the program containing the given class, and returns the IR of
     * each of its methods that has a body (constructors and static initializers are skipped).
     * The class is read from the same place the running JVM loads it from (e.g. build/classes/java/main
     * with Gradle, bin/main with VS Code), unless the system property "taie.classpath" is set.
     *
     * @param targetClass the fully qualified name of the class to analyze.
     * @return the IR of each analyzable method of the class.
     */
    public static List<IR> loadMethods(String targetClass) {
        String classPath = System.getProperty("taie.classpath", classPathOf(targetClass));
        String outputDir = System.getProperty("taie.outputDir", "build/tai-e");
        Main.buildWorld(
                "-cp", classPath,
                "--input-classes", targetClass,
                "--output-dir", outputDir);

        JClass jclass = World.get().getClassHierarchy().getClass(targetClass);
        if (jclass == null || jclass.isPhantom()) {
            throw new IllegalArgumentException("Class not found: " + targetClass + " (class path: " + classPath + ")");
        }
        List<IR> methods = new ArrayList<>();
        for (JMethod method : jclass.getDeclaredMethods()) {
            if (!method.isAbstract() && !method.isNative() && !method.isConstructor() && !method.isStaticInitializer()) {
                methods.add(method.getIR());
            }
        }
        return methods;
    }

    /**
     * Finds the class path entry (a directory or a jar) the running JVM loads the given class from.
     */
    private static String classPathOf(String className) {
        String resource = className.replace('.', '/') + ".class";
        URL url = TaieRunner.class.getClassLoader().getResource(resource);
        if (url == null) {
            throw new IllegalArgumentException("Class not found: " + className + " (is it compiled and on the class path?)");
        }
        try {
            if ("jar".equals(url.getProtocol())) {
                // jar:file:/path/to/app.jar!/pkg/Name.class
                String jar = url.getPath().substring(0, url.getPath().indexOf("!/"));
                return Paths.get(new URI(jar)).toString();
            }
            // file:/path/to/classes/pkg/Name.class -> /path/to/classes
            Path classFile = Paths.get(url.toURI());
            Path root = classFile;
            for (int i = 0; i < resource.split("/").length; i++) {
                root = root.getParent();
            }
            return root.toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Cannot locate the class path of " + className + ": " + url, e);
        }
    }

    /**
     * Builds the control-flow graph of a method. Each node is a statement of the IR, plus an
     * entry and an exit node.
     *
     * @param ir the IR of the method.
     * @return the control-flow graph.
     */
    public static CFG<Stmt> buildCFG(IR ir) {
        // "exception: null" leaves out exceptional control flow, which the targets don't need.
        return new CFGBuilder(AnalysisConfig.of(CFGBuilder.ID, "exception", null, "dump", false)).analyze(ir);
    }
}
