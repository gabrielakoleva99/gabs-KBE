package htwb.ai;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

//Main Class should find all declared methods in the class RunnableClass and list the
//method names which are not annotated with @RunMe and which are annotated with @RunMe.
//The methods annotated with @RunMe should be executed
//If these methods throw exceptions during execution, the framework must indicate which method was not
//executable and what error was thrown during execution.

public class Main {
    public static void main(String[] args){
        String className = null;
            try {
                //retrieve the class
                className = System.getProperty("classToRun");
                if (className == null) {
                    System.out.println("No class specified. Usage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar");
                    return;
                }

                //instantiate the class
                Class<?> runnableClass = Class.forName(className);

                //get all methods, methods with @RunMe and without and print out the findings
                System.out.println("Analyzed class '" + className + "':");
                Method[] declaredMethods = runnableClass.getDeclaredMethods();
                Method[] runnableMethods = getRunnableMethods(declaredMethods, true);
                Method[] otherMethods = getRunnableMethods(declaredMethods, false);
                System.out.println("Methods with @RunMe:");
                for (Method m : runnableMethods) {
                    System.out.println(m.getName());
                }
                System.out.println("Methods without @RunMe:");
                for (Method m : otherMethods) {
                    System.out.println(m.getName());
                }

                //Run all methods with @RunMe and handle eventually occurring exceptions accordingly
                System.out.println("Methods with @RunMe not invocable: ");
                try {
                    Object objKlass = runnableClass.getDeclaredConstructor().newInstance();
                    for (Method method : runnableMethods) {
                        try {
                            method.invoke(objKlass);
                        } catch (IllegalAccessException e) {
                            System.out.println(method.getName() + ": IllegalAccessException");
                        } catch (InvocationTargetException e) {
                            System.out.println(method.getName() + ": InvocationTargetException");
                        }
                    }
                } catch (InvocationTargetException e) {
                    System.out.println(runnableClass.getName() + ": InvocationTargetException");
                } catch (InstantiationException e) {
                    System.out.println(runnableClass.getName() + ": InstantiationException");
                } catch (IllegalAccessException e) {
                    System.out.println(runnableClass.getName() + ": IllegalAccessException");
                } catch (NoSuchMethodException e) {
                    System.out.println( "Could not instantiate class " +runnableClass.getName());
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Could not find class: " + className + "\nUsage: java -DclassToRun=your.package.ClassName -jar runmerunner-TEAMNAME.jar");
               // System.out.println(e.getMessage());
            }

        }

        /**
         * Returns all Methods with or without annotation @RunMe from  the provided array of methods. "withRunMe" specifies
         * if the ones with or without this annotation should be returned.
         * @param methods the methods that should be scanned for the annotation
         * @param withRunMe defines if methods with or without the annotation should be returned
         * @return the methods with or without annotations
         */
        static Method[] getRunnableMethods(Method[] methods, boolean withRunMe) {
            ArrayList<Method> requireMethods = new ArrayList<>();
            if (withRunMe) {
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RunMe.class)) {
                        requireMethods.add(method);
                    }
                }
            } else {
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(RunMe.class)) {
                        requireMethods.add(method);
                    }
                }
            }
            return requireMethods.toArray(new Method[0]);
        }
    }


