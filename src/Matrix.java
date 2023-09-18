import java.util.ArrayList;

public class Matrix {

    private int rows, cols;
    private ArrayList<ArrayList<Integer>> entries;

    // Constructor for rectangular matrix
    public Matrix(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        entries = new ArrayList<ArrayList<Integer>>();
        for(int i=0; i<rows; i++){
            entries.add(new ArrayList<Integer>());
            for(int j=0; j<cols; j++){
                ((ArrayList<Integer>) entries.get(i)).add(0);
            }
        }
    }

    // Constructor for square matrix
    public Matrix(int size){
        this.rows = size;
        this.cols = size;
        entries = new ArrayList<ArrayList<Integer>>();
        for(int i=0; i<rows; i++){
            entries.add(new ArrayList<Integer>());
            for(int j=0; j<cols; j++){
                ((ArrayList<Integer>) entries.get(i)).add(0);
            }
        }
    }

    public int rows(){
        return rows;
    }

    public int cols(){
        return rows;
    }

    public int get(int row, int col){
        return (Integer) ((ArrayList<Integer>) entries.get(row)).get(col);
    }

    public void set(int row, int col, int val){
        ((ArrayList<Integer>) entries.get(row)).set(col, val);
    }

    public void add(int row, int col, int add){
        ((ArrayList<Integer>) entries.get(row)).set(col, get(row, col) + add);
    }

    public String toString(){
        String result = "";
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                result += ((ArrayList<Integer>) entries.get(i)).get(j) + " ";
            }
            result += "\n";
        }
        return result;
    }

}