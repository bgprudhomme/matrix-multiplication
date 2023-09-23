import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class MatrixMultiplication {

    public static int mults;
    public static int adds;
    public static void main(String[] args){
        // System.out.print("Input: (T)ype or get from (F)ile? ");
        // Scanner in = new Scanner(System.in);

        // if(in.next().charAt(0) == 'T') {

        // }
        // else {
        //     System.out.print("Pathname: ");
        //     testFile(new File(in.next()));
        // }

        int[] sizes = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};

        for(int size : sizes){
            System.out.println("Size: " + size + "x" + size);
            testPerformance(new File("../test/" + size + "x" + size + "/all1s.txt"));
        }

    }

    public static void testPerformance(File f){
        try {
            Scanner in = new Scanner(f);
        
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

            mults = 0;
            adds = 0;
            Matrix c1 = multiplyDirect(a, b);
            System.out.println("Direct: " + adds + " additions, " + mults + " multiplications");
            
            mults = 0;
            adds = 0;
            Matrix c2 = multiplyStrassen(a, b);
            System.out.println("Strassen: " + adds + " additions, " + mults + " multiplications");

            try {
                assert(c1.equals(c2));



            } catch(AssertionError ae) {
                System.out.println("Error: Strassen incorrect for...");
                System.out.println("A:\n" + a);
                System.out.println("B:\n" + b);
                System.out.println("Direct:\n" + c1);
                System.out.println("Strassen:\n" + c2);

            }

            // System.out.println("Checked " + f);
            
        } catch(FileNotFoundException ex){
            System.out.println("Error: File not found");
        }
    }

    public static void testDirectory(File dir){
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null){
            for(int i=1; i<directoryListing.length; i++){
                System.out.println(directoryListing[i]);
                testDirectory(directoryListing[i]);
            }
        }
        else
            testPerformance(dir);
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
            for(int j=0; j<b.cols(); j++){
                c.set(i, j, a.get(i, j) - b.get(i, j));
                adds++;
            }
        }
        return c;
    }

    public static Matrix multiplyDirect(Matrix a, Matrix b){
        Matrix c = new Matrix(a.rows(), b.cols());
        for(int i=0; i<a.rows(); i++){
            for(int j=0; j<b.cols(); j++){
                    c.set(i, j, a.get(i, 0)*b.get(0, j));
                    mults++;
                for(int k=1; k<a.cols(); k++){
                    c.add(i, j, a.get(i, k)*b.get(k, j));
                    adds++;
                    mults++;
                }
            }
        }
        return c;
    }

    public static Matrix multiplyStrassen(Matrix a, Matrix b){
        Matrix c = new Matrix(a.rows(), a.cols());
        if(a.rows() == 1){
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

            Matrix c11 = new Matrix(a.rows()/2, a.cols()/2);
            Matrix c12 = new Matrix(a.rows()/2, a.cols()/2);
            Matrix c21 = new Matrix(a.rows()/2, a.cols()/2);
            Matrix c22 = new Matrix(a.rows()/2, a.cols()/2);

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
            c11 = add(subtract(add(p5, p4), p2), p6);
            c12 = add(p1, p2);
            c21 = add(p3, p4);
            c22 = subtract(subtract(add(p5, p1), p3), p7);

            c = new Matrix(c11, c12, c21, c22);

        }

        return c;

    }

}