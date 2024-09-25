import java.io.IOException;
import java.io.File;

public class Driver {
    public static void main(String[] args) {
        Polynomial p = new Polynomial();
        System.out.println(p.evaluate(3)); //Expected: 0.0

        double[] c1 = {6, 5}; 
        int[] e1 = {0, 1};
        Polynomial p1 = new Polynomial(c1, e1);

        System.out.println(p1.evaluate(1)); //Expected: 11.0

        double[] c2 = {-2, -9}; 
        int[] e2 = {1, 4}; 
        Polynomial p2 = new Polynomial(c2, e2);

        System.out.println(p2.evaluate(1)); //Expected: -11.0

        Polynomial s = p1.add(p2);
        System.out.println(s.evaluate(0.1)); //Expected: 6.2991

        if (s.hasRoot(1)) {
            System.out.println("1 is a root of s"); //<- Expected
        } else {
            System.out.println("1 is not a root of s"); //<- Not expected
        }   

        File textToRead = new File("test1.txt");
        try {
            Polynomial newPoly = new Polynomial(textToRead);
            System.out.println(newPoly.evaluate(1)); //Expected: 9.0
        } catch(IOException e) {
            System.out.println("Error: IOException");
        } 
        finally {
        }


        try {
            s.saveToFile("test3.txt");
        } catch(IOException e) {
            System.out.println("Error: IOException");
        } 
        finally {
            System.out.println("Finally executed");
        }
    }
}
