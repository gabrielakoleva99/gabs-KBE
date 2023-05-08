package htwb.ai;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static htwb.ai.Main.getRunnableMethods;

public class TestRunMeRunner {

    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setup() {
        // Redirect console output to capture it
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void noClassSpecifiedShouldReturnError() throws Exception {
        // Run the Main class
        Main.main(null);

        // Verify that the output contains the expected method names and error messages
        String expectedOutput = "No class specified. Usage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar";

        Assertions.assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test
    public void noClassFoundShouldReturnError() throws Exception {
        // Set the system property to point to a test class that doesn't exist
        System.setProperty("classToRun", "blub");

        // Run the Main class
        Main.main(null);

        // Verify that the output contains the expected method names and error messages
        String expectedOutput = "Could not find class: blub\nUsage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar";

        Assertions.assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

//    @Test
//  public void nonInstantiatableClassFoundShouldReturnError() throws Exception {
//      // Set the system property to point to a test class that doesn't exist
//      System.setProperty("classToRun", "java.io.Closeable");

//      // Run the Main class
//      Main.main(null);

//      // Verify that the output contains the expected method names and error messages
//      String expectedOutput = "Could not instantiate class java.io.Closeable java.io.Closeable\nUsage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar";

//      Assertions.assertEquals(expectedOutput.trim(), outContent.toString().trim());
//  }

    @Test
    public void methodsWithRunMeShouldReturn6() throws Exception {
        // Set the system property to point to the test class with annotated methods
        System.setProperty("classToRun", "htwb.ai.RunnableClass");

        // Run the Main class
        Main.main(null);

        // Verify that the output contains the right amount of methods
        Class<?> runnableClass = Class.forName("htwb.ai.RunnableClass");
        Method[] declaredMethods = runnableClass.getDeclaredMethods();
        Method[] runnableMethods = getRunnableMethods(declaredMethods, true);

        Assertions.assertEquals(6, runnableMethods.length);
    }

    @Test
    public void methodsWithoutRunMeShouldReturn3() throws Exception {
        // Set the system property to point to the test class with annotated methods
        System.setProperty("classToRun", "htwb.ai.RunnableClass");

        // Run the Main class
        Main.main(null);

        // Verify that the output contains the right amount of methods
        Class<?> runnableClass = Class.forName("htwb.ai.RunnableClass");
        Method[] declaredMethods = runnableClass.getDeclaredMethods();
        Method[] nonRunnableMethods = getRunnableMethods(declaredMethods, false);

        Assertions.assertEquals(3, nonRunnableMethods.length);
    }
}
