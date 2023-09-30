import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
// import java.util.concurrent.ThreadLocalRandom;

public class MatrixRandGen {
    public static void main(String[] args) {
        File f;
        BufferedWriter w;
        int min = -100;
        int max = 100;

        int maxK = 10;

        for(int k = 0; k<= maxK; k++){
            try {
                int s = 3 * (int) Math.pow(2, k-1);
                System.out.println("Size: " + s + "x" + s);

                f = new File("../test/performanceEvalExtra3/" + s + "x" + s + ".txt");
                
                    w = new BufferedWriter(new FileWriter(f, true));
                    w.write(s + "\n");
                    for(int i=0; i<2*s; i++){
                        for(int j=0; j<s; j++){
                            w.write("1 ");
                        }
                        w.write("\n");
                    }
                
                System.out.println("File created: ../test/performanceEvalExtra3/" + s + "x" + s + ".txt");
                w.close();
                
                
            } catch (IOException e){
                    System.out.println("IOException occurred");
                }
        }
    }
}