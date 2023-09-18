import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class MatrixMultiplication {
    public static void main(String[] args){
        Matrix a, b;

        File f = new File("../test/1.txt");
        try {
            Scanner in = new Scanner(f);
        
            int size = in.nextInt();

            a = new Matrix(size);
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    a.set(i, j, in.nextInt());
                }
            }

            b = new Matrix(size);
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    b.set(i, j, in.nextInt());
                }
            }

            in.close();

            System.out.println(a);
            System.out.println(b);

            System.out.println(multiplyDirect(a, b));

        } catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }

    public static void readFromFile(String path, Matrix a, Matrix b){
        
    }

    public static Matrix multiplyDirect(Matrix a, Matrix b){
        Matrix result = new Matrix(a.rows(), b.cols());
        for(int i=0; i<a.rows(); i++){
            for(int j=0; j<b.cols(); j++){
                for(int k=0; k<a.cols(); k++){
                    result.add(i, j, a.get(i, k)*b.get(k, j));
                }
            }
        }
        return result;
    }
}