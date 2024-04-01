public class PageReference {
    private String matrix;
    private int row;
    private int col;
    private int pageNumber;
    private int offset;
    private char action;

    public PageReference(String matrix, int row, int col, int pageNumber, int offset, char action) {
        this.matrix = matrix;
        this.row = row;
        this.col = col;
        this.pageNumber = pageNumber;
        this.offset = offset;
        this.action = action;
    }

    public String getMatrix() {
        return matrix;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public char getAction() {
        return action;
    }

    public void setAction(char action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return matrix + "[" + row + "][" + col + "]," + pageNumber + "," + offset + "," + action;
    }
}