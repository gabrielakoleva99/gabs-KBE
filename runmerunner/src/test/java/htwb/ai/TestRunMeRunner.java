package htwb.ai;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;


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
    public void nonInstantiatableClassFoundShouldReturnError() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // Set the system property to point to a test class that cant be instantiated
        System.setProperty("classToRun", "java.io.Closeable");

        // Run the Main class
        Main.main(null);

        // Verify that the output contains the expected method names and error messages
        String expectedOutput = "Analyzed class 'java.io.Closeable':"+System.lineSeparator()+"Could not instantiate class java.io.Closeable"+System.lineSeparator()+"Usage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar";

        Assertions.assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    @Test
    public void nonInstantiatableClassShouldReturnExceptionError() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // Set the system property to point to a test class that cant be instantiated
        System.setProperty("classToRun", "java.lang.Number");

        // Run the Main class
        Main.main(null);

        // Verify that the output contains the expected method names and error messages
        String expectedOutput = "Analyzed class 'java.lang.Number':"
                +System.lineSeparator()+"java.lang.Number: InstantiationException"
                +System.lineSeparator()+
                "Usage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar";

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

    @Test
    public void testHasMethodsWithAnnotation() {
        Class<Main> resourceClass = Main.class;
        Method[] methods = resourceClass.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(RunMe.class)) {
                Assertions.assertEquals("RunMe", Arrays.toString(m.getAnnotations()));
            }
        }
    }

    @Test
    public void testShowMethodsWithAnnotation() {
        Class<Main> c = Main.class;
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(RunMe.class)) {
                ArrayList<Method> methodsWithRunMe = new ArrayList<>();
                methodsWithRunMe.add(m);
                String s = "findme1" + System.lineSeparator() + "findme2" + System.lineSeparator()
                        +"findme3" + System.lineSeparator() +"findme4" + System.lineSeparator()
                        +"findme5" + System.lineSeparator() +"findme6" + System.lineSeparator();

                Assertions.assertEquals(s, methodsWithRunMe.toString());
            }
        }
    }

}