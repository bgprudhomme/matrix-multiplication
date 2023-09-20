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

    // Constructor for matrix composed from 2x2 partitioning of smaller matrices
    public Matrix (Matrix c11, Matrix c12, Matrix c21, Matrix c22) {
        this.rows = c11.rows() + c21.rows();
        this.cols = c11.cols() + c12.cols();
        entries = new ArrayList<ArrayList<Integer>>();

        for(int i=0; i<c11.rows(); i++){
            entries.add(new ArrayList<Integer>());
            for(int j=0; j<c11.cols(); j++){
                ((ArrayList<Integer>) entries.get(i)).add(c11.get(i, j));
            }
            for(int j=0; j<c12.cols(); j++){
                ((ArrayList<Integer>) entries.get(i)).add(c12.get(i, j));
            }
        }
        for(int i=0; i<c21.rows(); i++){
            entries.add(new ArrayList<Integer>());
            for(int j=0; j<c21.cols(); j++){
                ((ArrayList<Integer>) entries.get(i+c11.rows())).add(c21.get(i, j));
            }
            for(int j=0; j<c22.cols(); j++){
                ((ArrayList<Integer>) entries.get(i+c11.rows())).add(c22.get(i, j));
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

    public Matrix partition(int startRow, int endRow, int startCol, int endCol){
        Matrix result = new Matrix(endRow-startRow, endCol-startCol);
        for(int i=0; i<endRow-startRow; i++){
            for(int j=0; j<endCol-startCol; j++){
                result.set(i, j, this.get(startRow+i, startCol+j));
            }
        }
        return result;
    }

    public boolean equals(Matrix other){
        if((this.rows() != other.rows()) || (this.cols() != other.cols()))
            return false;
        for(int i=0; i<this.rows(); i++){
            for(int j=0; j<this.rows(); j++){
                if(this.get(i, j) != other.get(i, j))
                    return false;
            }
        }
        return true;
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