import java.io.BufferedWriter;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;

public class MatrixMultiplication {

    public static int mults;
    public static int adds;
    public static void main(String[] args){

        takeInputFromTerminal();

        // takeInputFromFile(new File("../test/16x16/1.txt"));

        // performanceEvalOperations();

        // performanceEvalRuntime();

    }

    public static void takeInputFromTerminal(){
        System.out.println("Input:");
        multiplyAndPrint(new Scanner(System.in));
    }

    public static void takeInputFromFile(File f){
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

        in.close();

        System.out.println("Direct:");
        Matrix c = multiplyDirect(a, b);

        if(size <= 16)
            System.out.println(c + "=\n" + a + "*\n" + b);
        else {
            try {
                BufferedWriter w = new BufferedWriter(new FileWriter(new File("../out.txt"), true));
                w.write(c.toString());
                w.close();

            } catch(IOException e){e.printStackTrace();}
        }

        System.out.println("Strassen:");
        c = multiplyStrassen(a, b);

        if(size <= 16)
            System.out.println(c + "=\n" + a + "*\n" + b);
        else {
            try {
                BufferedWriter w = new BufferedWriter(new FileWriter(new File("../out.txt"), true));
                w.write(c.toString());
                w.close();

            } catch(IOException e){e.printStackTrace();}
        }

        System.out.println("Hybrid:");
        c = multiplyHybrid(a, b, 1024);

        if(size <= 16)
            System.out.println(c + "=\n" + a + "*\n" + b);
        else {
            try {
                BufferedWriter w = new BufferedWriter(new FileWriter(new File("../out.txt"), true));
                w.write(c.toString());
                w.close();

            } catch(IOException e){e.printStackTrace();}
        }
    }

    public static void performanceEvalOperations(){
        int dAdds = 0;
        int dMults = 0;
        int sAdds = 0;
        int sMults = 0;
        int hAdds = 0;
        int hMults = 0;

        System.out.println(" k    n      + (D)      * (D)    Tot (D)      + (S)      * (S)    Tot (S)      + (H)      * (H)    Tot (H)");

        // try {
            for(int k=0; k<=20; k++){

                int size =  ((Double) Math.pow(2, k)).intValue();

                // File f = new File("../test/" + size + "x" + size + "/all1s.txt");

                //Scanner in = new Scanner(f);
            
                //in.nextInt();

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

                // in.close();

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

                System.out.printf("%2d%5d%11d%11d%11d%11d%11d%11d%11d%11d%11d\n", k, size, dAdds, dMults, dAdds+dMults, sAdds, sMults, sAdds+sMults, hAdds, hMults, hAdds+hMults);

            }
            
        // } catch(FileNotFoundException ex){
        //     System.out.println("Error: File not found");
        // }
    }

    public static void performanceEvalRuntime(){
        long dStart, dEnd, sStart, sEnd;

        System.out.println(" k    n      ms (D)    ms (S)");

        // try {
            for(int k=0; k<=20; k++){

                int size =  ((Double) Math.pow(2, k)).intValue();

                // File f = new File("../test/" + size + "x" + size + "/all1s.txt");

                // Scanner in = new Scanner(f);
            
                // in.nextInt();

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

                // in.close();

                dStart = System.currentTimeMillis();
                Matrix c1 = multiplyDirect(a, b);
                dEnd = System.currentTimeMillis();

                sStart = System.currentTimeMillis();
                Matrix c2 = multiplyStrassen(a, b);
                sEnd = System.currentTimeMillis();

                try {
                    assert(c1.equals(c2));



                } catch(AssertionError ae) {
                    System.out.println("Error: Strassen incorrect for...");
                    System.out.println("A:\n" + a);
                    System.out.println("B:\n" + b);
                    System.out.println("Direct:\n" + c1);
                    System.out.println("Strassen:\n" + c2);

                }

                System.out.printf("%2d%5d%11d%11d\n", k, size, dEnd-dStart, sEnd-sStart);

            }
            
        // } catch(FileNotFoundException ex){
        //     System.out.println("Error: File not found");
        // }
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
        long startTime = System.currentTimeMillis();
        Matrix c = new Matrix(a.rows(), b.cols());
        for(int i=0; i<a.rows(); i++){
            for(int j=0; j<b.cols(); j++){
                    c.set(i, j, a.get(i, 0)*b.get(0, j));
                    mults++;
                    // System.out.println("(" + i + ", " + j + ", 0): " + (System.currentTimeMillis()-startTime));
                for(int k=1; k<a.cols(); k++){
                    c.add(i, j, a.get(i, k)*b.get(k, j));
                    adds++;
                    mults++;
                    // System.out.println("(" + i + ", " + j + ", " + k + "): " + (System.currentTimeMillis()-startTime));
                }
            }
        }
        return c;
    }

    public static Matrix multiplyStrassen(Matrix a, Matrix b){
        // long startTime = System.currentTimeMillis();
        // long step1Time = 0, step2Time = 0, step3Time = 0, step4Time = 0;
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

            // step1Time = System.currentTimeMillis()-startTime;

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

            // step2Time = System.currentTimeMillis()-startTime;

            // Step 3
            Matrix p1 = multiplyStrassen(a11, s1);
            Matrix p2 = multiplyStrassen(s2, b22);
            Matrix p3 = multiplyStrassen(s3, b11);
            Matrix p4 = multiplyStrassen(a22, s4);
            Matrix p5 = multiplyStrassen(s5, s6);
            Matrix p6 = multiplyStrassen(s7, s8);
            Matrix p7 = multiplyStrassen(s9, s10);

            // step3Time = System.currentTimeMillis()-startTime;

            // Step 4
            c = new Matrix(add(subtract(add(p5, p4), p2), p6), add(p1, p2), add(p3, p4), subtract(subtract(add(p5, p1), p3), p7));
            // step4Time = System.currentTimeMillis()-startTime;

        }

        // System.out.println(step1Time + ", " + step2Time + ", " + step3Time + ", " + step4Time);

        return c;

    }

    public static Matrix multiplyHybrid(Matrix a, Matrix b, int switchThreshold){
        int size = a.rows();
        Matrix c;
        if(size < switchThreshold){
            c = multiplyDirect(a, b);
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

            // step1Time = System.currentTimeMillis()-startTime;

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

            // step2Time = System.currentTimeMillis()-startTime;

            // Step 3
            Matrix p1 = multiplyHybrid(a11, s1, switchThreshold);
            Matrix p2 = multiplyHybrid(s2, b22, switchThreshold);
            Matrix p3 = multiplyHybrid(s3, b11, switchThreshold);
            Matrix p4 = multiplyHybrid(a22, s4, switchThreshold);
            Matrix p5 = multiplyHybrid(s5, s6, switchThreshold);
            Matrix p6 = multiplyHybrid(s7, s8, switchThreshold);
            Matrix p7 = multiplyHybrid(s9, s10, switchThreshold);

            // step3Time = System.currentTimeMillis()-startTime;

            // Step 4
            c = new Matrix(add(subtract(add(p5, p4), p2), p6), add(p1, p2), add(p3, p4), subtract(subtract(add(p5, p1), p3), p7));
        }
        return c;
    }

}