package htwb.ai;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;


public class TestRunMeRunner {

    private ByteArrayOutputStream outContent;

    @Test
    public void noInputShouldReturnError() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        System.setProperty("classToRun","");
        // Run the Main class
        Main.main(null);

        // Verify that the output contains the expected method names and error messages
        String expectedOutput = "No classname specified" + System.lineSeparator()+ "Usage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar";

        Assertions.assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test
    public void noClassFoundShouldReturnError() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // Set the system property to point to a test class that doesn't exist
        System.setProperty("classToRun", "blub");

        // Run the Main class
        Main.main(null);

        // Verify that the output contains the expected method names and error messages
        String expectedOutput = "Analyzed class 'blub':" + System.lineSeparator()+ "Could not find class: blub" + System.lineSeparator()+ "Usage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar";

        Assertions.assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test
    public void nonInstantiatableClassFoundShouldReturnError() throws Exception {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // Set the system property to point to a test class that doesn't exist
        System.setProperty("classToRun", "java.io.Closeable");

        // Run the Main class
        Main.main(null);

        // Verify that the output contains the expected method names and error messages
        String expectedOutput = "Analyzed class 'java.io.Closeable':"+System.lineSeparator()+"Could not instantiate class java.io.Closeable"+System.lineSeparator()+"Usage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar";

        Assertions.assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test
    void testGetRunnableMethods() {
        Class<?> mockClass = new Object() {
            @RunMe
            public void methodPublic() {
            }

            @RunMe
            public void methodPublic2() {
            }

            public void methodNotAnnotated() {
            }
        }.getClass();
        // Call the getRunnableMethods method of Main on the mock class
        Method[] runnableMethods = Main.getRunnableMethods(mockClass.getDeclaredMethods(), true);

        // Check that the correct number of methods with @RunMe are returned
        Assertions.assertEquals(2, runnableMethods.length);
    }

    @Test
    void testGetNonRunnableMethods() {
        Class<?> mockClass = new Object() {
            public void methodNotAnnotated() {
            }
        }.getClass();
        // Call the getRunnableMethods method of Main on the mock class
        Method[] runnableMethods = Main.getRunnableMethods(mockClass.getDeclaredMethods(), false);

        // Check that the correct number of methods with @RunMe are returned
        Assertions.assertEquals(2, runnableMethods.length);
    }

}