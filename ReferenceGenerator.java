import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReferenceGenerator {
    private int pageSize;
    private int numRows;
    private int numCols;
    private int filterSize;
    private int numReferences;
    private int numPages;
    private List<String> references;

    public ReferenceGenerator(int pageSize, int numRows, int numCols) {
        this.pageSize = pageSize;
        this.numRows = numRows;
        this.numCols = numCols;
        this.filterSize = 3;
        this.references = new ArrayList<>();
        generateReferences();
    }

    private void generateReferences() {
        int bytesPerInt = 4;
        int filterSize = this.filterSize * this.filterSize * bytesPerInt;
        int totalSize = filterSize + 2 * numRows * numCols * bytesPerInt;
        numPages = (int) Math.ceil((double) totalSize / pageSize);

        int pageIndex = 0;
        int offset = 0;

        // Generar referencias para el filtro
        for (int i = 0; i < this.filterSize; i++) {
            for (int j = 0; j < this.filterSize; j++) {
                String reference = String.format("F[%d][%d],%d,%d,R", i, j, pageIndex, offset);
                references.add(reference);
                offset += bytesPerInt;
                if (offset >= pageSize) {
                    pageIndex++;
                    offset = 0;
                }
            }
        }

        // Generar referencias para la matriz de datos
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                String reference = String.format("M[%d][%d],%d,%d,R", i, j, pageIndex, offset);
                references.add(reference);
                offset += bytesPerInt;
                if (offset >= pageSize) {
                    pageIndex++;
                    offset = 0;
                }
            }
        }

        // Generar referencias para la matriz de resultado
        for (int i = 0; i < numRows - filterSize + 1; i++) {
            for (int j = 0; j < numCols - filterSize + 1; j++) {
                // Generar referencias para los elementos del filtro
                for (int k = 0; k < filterSize; k++) {
                    for (int l = 0; l < filterSize; l++) {
                        int rowIndex = i + k;
                        int colIndex = j + l;
                        String reference = String.format("F[%d][%d],%d,%d,R", k, l, getPageIndex(rowIndex, colIndex), getOffset(rowIndex, colIndex));
                        references.add(reference);
                    }
                }

                // Generar referencia para escribir en la matriz de resultado
                String reference = String.format("R[%d][%d],%d,%d,W", i, j, pageIndex, offset);
                references.add(reference);
                offset += bytesPerInt;
                if (offset >= pageSize) {
                    pageIndex++;
                    offset = 0;
                }
            }
        }

        numReferences = references.size();
    }

    private int getPageIndex(int rowIndex, int colIndex) {
        int bytesPerInt = 4;
        int filterSize = this.filterSize * this.filterSize * bytesPerInt;
        int position = filterSize + (rowIndex * numCols + colIndex) * bytesPerInt;
        return position / pageSize;
    }

    private int getOffset(int rowIndex, int colIndex) {
        int bytesPerInt = 4;
        int filterSize = this.filterSize * this.filterSize * bytesPerInt;
        int position = filterSize + (rowIndex * numCols + colIndex) * bytesPerInt;
        return position % pageSize;
    }

    public void writeReferencesToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("TP=" + pageSize + "\n");
            writer.write("NF=" + numRows + "\n");
            writer.write("NC=" + numCols + "\n");
            writer.write("NF_NC_Filtro=" + filterSize + "\n");
            writer.write("NR=" + numReferences + "\n");
            writer.write("NP=" + numPages + "\n");

            for (String reference : references) {
                writer.write(reference + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}