package htwb.ai;

//hier Methoden mit @RunMe Annotation und ohne schreiben
public class RunnableClass {

    public RunnableClass () {}

    public void testWithoutRM(){
        System.out.println("testWithoutRM");
    }

    public void testNoRM22(){
         System.out.println("testNoRM22");
     }

    @RunMe
    public void findMe1(){
         System.out.println("findMe1");
     }

    @RunMe
    public void findMe2(){
        System.out.println("findMe2");
    }

    @RunMe
    private void findMe3(){
        System.out.println("findMe3");
    }

    @RunMe
    public void findMe4(){
        System.out.println("findMe4");
    }

    @RunMe
    public void findMe5(){
        System.out.println("findMe5");
    }
    @RunMe
    public void findMe6(){
        System.out.println("findMe6");
    }
}
