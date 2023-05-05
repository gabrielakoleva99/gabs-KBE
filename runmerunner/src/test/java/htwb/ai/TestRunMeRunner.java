package htwb.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class TestRunMeRunner {

    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testRunWithNoArgs() {
        Main.main(new String[]{});
        String expectedOutput = "Usage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testRunWithNonexistentClass() {
        Main.main(new String[]{"-DclassToRun=nonexistent.class"});
        String expectedOutput = "Could not find class: nonexistent.class\n" +
                "Usage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testRunWithNonInstantiableClass() {
        Main.main(new String[]{"-DclassToRun=java.io.Closeable"});
        String expectedOutput = "Could not instantiate class java.io.Closeable\n" +
                "Usage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testRunWithValidClassWithoutRunMeMethods() {
        Main.main(new String[]{"-DclassToRun=htwb.ai.NoRunMeClass"});
        String expectedOutput = "------------ Analyzed htwb.ai.NoRunMeClass ------------\n" +
                "Methods without @RunMe:\n" +
                "method1\n" +
                "method2\n" +
                "method3\n" +
                "method4\n" +
                "Methods with @RunMe:\n" +
                "Methods with @RunMe not invocable:\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testRunWithValidClassWithRunMeMethods() {
        Main.main(new String[]{"-DclassToRun=htwb.ai.RunMeClass"});
        String expectedOutput = "------------ Analyzed htwb.ai.RunMeClass ------------\n" +
                "Methods without @RunMe:\n" +
                "method1\n" +
                "method3\n" +
                "Methods with @RunMe:\n" +
                "method2\n" +
                "method4\n" +
                "Methods with @RunMe not invocable:\n" +
                "method4: java.lang.RuntimeException: This method throws a RuntimeException\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testRunWithNoClassSpecified() {
        Main.main(new String[]{});
        String expectedOutput = "No class specified. Usage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    
}
