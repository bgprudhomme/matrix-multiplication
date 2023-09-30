import java.io.BufferedWriter;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;

public class MatrixMultiplication {

    public static long mults;
    public static long adds;
    public static void main(String[] args){

        // takeInputFromTerminal();

        // takeInputFromFile();

        performanceEvalOperations();

        // performanceEvalRuntime();

        // correctnessTest();
    }

    public static void takeInputFromTerminal(){
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println("Input:");
            multiplyAndPrint(in);
        }
    }

    public static void takeInputFromFile(){
        System.out.print("File: ");
        Scanner in = new Scanner(System.in);
        File f = new File(in.nextLine());
        in.close();
        try {
            multiplyAndPrint(new Scanner(f));
        } catch(FileNotFoundException e ){e.printStackTrace();}

    }

    public static void multiplyAndPrint(Scanner in){
        int size = in.nextInt();

        Matrix a = new Matrix(size);
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                a.set(i, j, in.nextInt());
            }
        }

        Matrix b = new Matrix(size);
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                b.set(i, j, in.nextInt());
            }
        }

        Matrix c1 = multiplyDirect(a, b);
        Matrix c2 = multiplyStrassen(a, b);
        Matrix c3 = multiplyHybrid(a, b, 1024);
        try {
            assert(c1.equals(c2) && c1.equals(c3));
            if(size <= 16)
                System.out.println(c1 + "=\n" + a + "*\n" + b);
            else {
                try {
                    BufferedWriter w = new BufferedWriter(new FileWriter(new File("../out.txt"), false));
                    w.write(c1.toString());
                    w.close();
                    System.out.println("Multiplication output in ../out.txt");
                } catch(IOException e){e.printStackTrace();}
            }
        } catch(AssertionError ae){
            System.out.println("Error: Inconsistent results for...");
            System.out.println("A:\n" + a);
            System.out.println("B:\n" + b);
            System.out.println("Direct:\n" + c1);
            System.out.println("Strassen:\n" + c2);
            System.out.println("Hybrid:\n");
        }
    }

    public static void performanceEvalOperations(){
        long dAdds = 0;
        long dMults = 0;
        long sAdds = 0;
        long sMults = 0;
        long hAdds = 0;
        long hMults = 0;

        System.out.println(" k    n       + (D)       * (D)     Tot (D)       + (S)       * (S)     Tot (S)       + (H)       * (H)     Tot (H)");

        for(int k=0; k<=11; k++){ // On my machine, OutOfMemoryError occurs for k>=12

            int size = (int) Math.pow(2, k);

            Matrix a = new Matrix(size);
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    a.set(i, j, 1);
                }
            }

            Matrix b = new Matrix(size);
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    b.set(i, j, 1);
                }
            }

            mults = 0;
            adds = 0;
            Matrix c1 = multiplyDirect(a, b);
            dAdds = adds;
            dMults = mults;

            mults = 0;
            adds = 0;
            Matrix c2 = multiplyStrassen(a, b);
            sAdds = adds;
            sMults = mults;

            mults = 0;
            adds = 0;
            Matrix c3 = multiplyHybrid(a, b, 1024);
            hAdds = adds;
            hMults = mults;

            try {
                assert(c1.equals(c2) && c1.equals(c3));
            } catch(AssertionError ae) {
                System.out.println("Error: Inconsistent results for...");
                System.out.println("A:\n" + a);
                System.out.println("B:\n" + b);
                System.out.println("Direct:\n" + c1);
                System.out.println("Strassen:\n" + c2);
                System.out.println("Hybrid:\n");

            }

            System.out.printf("%2d%5d%12d%12d%12d%12d%12d%12d%12d%12d%12d\n", k, size, dAdds, dMults, dAdds+dMults, sAdds, sMults, sAdds+sMults, hAdds, hMults, hAdds+hMults);

        }
    }

    public static void performanceEvalRuntime(){
        long dStart, dEnd, sStart, sEnd, hStart, hEnd;

        System.out.println(" k    n      ms (D)    ms (S)    ms(H)");

        for(int k=0; k<=20; k++){

            int size = (int) Math.pow(2, k);

            Matrix a = new Matrix(size);
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    a.set(i, j, 1);
                }
            }

            Matrix b = new Matrix(size);
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    b.set(i, j, 1);
                }
            }

            Matrix c = new Matrix(size);
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    b.set(i, j, 1);
                }
            }

            dStart = System.currentTimeMillis();
            Matrix c1 = multiplyDirect(a, b);
            dEnd = System.currentTimeMillis();

            sStart = System.currentTimeMillis();
            Matrix c2 = multiplyStrassen(a, b);
            sEnd = System.currentTimeMillis();

            hStart = System.currentTimeMillis();
            Matrix c3 = multiplyHybrid(a, b, 1024);
            hEnd = System.currentTimeMillis();

            try {
                assert(c1.equals(c2) && c1.equals(c3));
            } catch(AssertionError ae) {
                System.out.println("Error: Strassen incorrect for...");
                System.out.println("A:\n" + a);
                System.out.println("B:\n" + b);
                System.out.println("Direct:\n" + c1);
                System.out.println("Strassen:\n" + c2);

            }

            System.out.printf("%2d%5d%11d%11d%9d\n", k, size, dEnd-dStart, sEnd-sStart, hEnd-hStart);

        }
    }

    public static void correctnessTest(){
        int size;
        Matrix a = new Matrix(3);
        Matrix b = new Matrix(3);
        Matrix c1 = new Matrix(3);
        Matrix c2 = new Matrix(3);
        Matrix c3 = new Matrix(3);
        try {
            for(size=3; size<=20; size++){
                a = new Matrix(size);
                for(int i=0; i<size; i++){
                    for(int j=0; j<size; j++){
                        a.set(i, j, 1);
                    }
                }

                b = new Matrix(size);
                for(int i=0; i<size; i++){
                    for(int j=0; j<size; j++){
                        b.set(i, j, 1);
                    }
                }

                mults = 0;
                adds = 0;
                c1 = multiplyDirect(a, b);

                mults = 0;
                adds = 0;
                c2 = multiplyStrassen(a, b);

                mults = 0;
                adds = 0;
                c3 = multiplyHybrid(a, b, 1024);
                
                assert(c1.equals(c2) && c1.equals(c3));

                System.out.println(c1.equals(c2) && c1.equals(c3));
            }
            System.out.println("All algorithms correct up to 20x20");
        } catch(AssertionError ae){
            System.out.println("Error: Inconsistent results for...");
            System.out.println("A:\n" + a);
            System.out.println("B:\n" + b);
            System.out.println("Direct:\n" + c1);
            System.out.println("Strassen:\n" + c2);
            System.out.println("Hybrid:\n" + c3);

        }
    }

    public static Matrix add(Matrix a, Matrix b){
        Matrix c = new Matrix(a.rows(), a.cols());
        for(int i=0; i<a.rows(); i++){
            for(int j=0; j<b.cols(); j++){
                c.set(i, j, a.get(i, j) + b.get(i, j));
                adds++;
            }
        }
        return c;
    }

    public static Matrix subtract(Matrix a, Matrix b){
        Matrix c = new Matrix(a.rows(), a.cols());
        for(int i=0; i<a.rows(); i++){
            for(int j=0; j<a.cols(); j++){
                c.set(i, j, a.get(i, j) - b.get(i, j));
                adds++;
            }
        }
        return c;
    }

    public static Matrix multiplyDirect(Matrix a, Matrix b){
        int size = a.rows();
        
        Matrix c = new Matrix(size);
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                    c.set(i, j, a.get(i, 0)*b.get(0, j));
                    mults++;
                for(int k=1; k<size; k++){
                    c.add(i, j, a.get(i, k)*b.get(k, j));
                    adds++;
                    mults++;
                }
            }
        }
        return c;
    }

    public static Matrix multiplyStrassen(Matrix a, Matrix b){
        int size = a.rows();
        int nextPowerOf2 = 1;
        while(nextPowerOf2 < size){
            nextPowerOf2 *= 2;
        }
        if(size != nextPowerOf2){;
            a = new Matrix(a, new Matrix(size, nextPowerOf2-size), new Matrix(nextPowerOf2-size, size), new Matrix(nextPowerOf2-size, nextPowerOf2-size));
            b = new Matrix(b, new Matrix(size, nextPowerOf2-size), new Matrix(nextPowerOf2-size, size), new Matrix(nextPowerOf2-size, nextPowerOf2-size));
        }
        Matrix c;
        if(a.rows() == 1){
            c = new Matrix(1);
            c.set(0, 0, a.get(0, 0) * b.get(0, 0));
            mults++;
        }
        else {
            // Step 1
            Matrix a11 = a.partition(0, a.rows()/2, 0, a.cols()/2);
            Matrix a12 = a.partition(0, a.rows()/2, a.cols()/2, a.cols());
            Matrix a21 = a.partition(a.rows()/2, a.rows(), 0, a.cols()/2);
            Matrix a22 = a.partition(a.rows()/2, a.rows(), a.cols()/2, a.cols());
            Matrix b11 = b.partition(0, b.rows()/2, 0, b.cols()/2);
            Matrix b12 = b.partition(0, b.rows()/2, b.cols()/2, b.cols());
            Matrix b21 = b.partition(b.rows()/2, b.rows(), 0, b.cols()/2);
            Matrix b22 = b.partition(b.rows()/2, b.rows(), b.cols()/2, b.cols());

            // Step 2
            Matrix s1 = subtract(b12, b22);
            Matrix s2 = add(a11, a12);
            Matrix s3 = add(a21, a22);
            Matrix s4 = subtract(b21, b11);
            Matrix s5 = add(a11, a22);
            Matrix s6 = add(b11, b22);
            Matrix s7 = subtract(a12, a22);
            Matrix s8 = add(b21, b22);
            Matrix s9 = subtract(a11, a21);
            Matrix s10 = add(b11, b12);

            // Step 3
            Matrix p1 = multiplyStrassen(a11, s1);
            Matrix p2 = multiplyStrassen(s2, b22);
            Matrix p3 = multiplyStrassen(s3, b11);
            Matrix p4 = multiplyStrassen(a22, s4);
            Matrix p5 = multiplyStrassen(s5, s6);
            Matrix p6 = multiplyStrassen(s7, s8);
            Matrix p7 = multiplyStrassen(s9, s10);

            // Step 4
            c = new Matrix(add(subtract(add(p5, p4), p2), p6), add(p1, p2), add(p3, p4), subtract(subtract(add(p5, p1), p3), p7));
            c = c.partition(0, size, 0, size);

        }

        return c;

    }

    public static Matrix multiplyHybrid(Matrix a, Matrix b, int switchThreshold){
        int size = a.rows();
        Matrix c;
        if(size < switchThreshold){
            c = multiplyDirect(a, b);
        }
        else {
            int nextPowerOf2 = 1;
            while(nextPowerOf2 < size){
                nextPowerOf2 *= 2;
            }
            if(size != nextPowerOf2){;
                a = new Matrix(a, new Matrix(size, nextPowerOf2-size), new Matrix(nextPowerOf2-size, size), new Matrix(nextPowerOf2-size, nextPowerOf2-size));
                b = new Matrix(b, new Matrix(size, nextPowerOf2-size), new Matrix(nextPowerOf2-size, size), new Matrix(nextPowerOf2-size, nextPowerOf2-size));
            }

            // Step 1
            Matrix a11 = a.partition(0, a.rows()/2, 0, a.cols()/2);
            Matrix a12 = a.partition(0, a.rows()/2, a.cols()/2, a.cols());
            Matrix a21 = a.partition(a.rows()/2, a.rows(), 0, a.cols()/2);
            Matrix a22 = a.partition(a.rows()/2, a.rows(), a.cols()/2, a.cols());
            Matrix b11 = b.partition(0, b.rows()/2, 0, b.cols()/2);
            Matrix b12 = b.partition(0, b.rows()/2, b.cols()/2, b.cols());
            Matrix b21 = b.partition(b.rows()/2, b.rows(), 0, b.cols()/2);
            Matrix b22 = b.partition(b.rows()/2, b.rows(), b.cols()/2, b.cols());

            // Step 2
            Matrix s1 = subtract(b12, b22);
            Matrix s2 = add(a11, a12);
            Matrix s3 = add(a21, a22);
            Matrix s4 = subtract(b21, b11);
            Matrix s5 = add(a11, a22);
            Matrix s6 = add(b11, b22);
            Matrix s7 = subtract(a12, a22);
            Matrix s8 = add(b21, b22);
            Matrix s9 = subtract(a11, a21);
            Matrix s10 = add(b11, b12);

            // Step 3
            Matrix p1 = multiplyHybrid(a11, s1, switchThreshold);
            Matrix p2 = multiplyHybrid(s2, b22, switchThreshold);
            Matrix p3 = multiplyHybrid(s3, b11, switchThreshold);
            Matrix p4 = multiplyHybrid(a22, s4, switchThreshold);
            Matrix p5 = multiplyHybrid(s5, s6, switchThreshold);
            Matrix p6 = multiplyHybrid(s7, s8, switchThreshold);
            Matrix p7 = multiplyHybrid(s9, s10, switchThreshold);

            // Step 4
            c = new Matrix(add(subtract(add(p5, p4), p2), p6), add(p1, p2), add(p3, p4), subtract(subtract(add(p5, p1), p3), p7));
            c = c.partition(0, size, 0, size);
        }
        return c;
    }

}