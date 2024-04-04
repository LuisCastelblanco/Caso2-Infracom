import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReferenciaGenerator {
    private int pageSize;
    private int numRows;
    private int numCols;
    private int filterSize;
    private int numReferences;
    private int numPages;
    private List<String> references;
    public String[][] filtro = new String[3][3];
    public String[][] matriz;
    public String[][] resultado;
    public int[][] mat3;
    public int[][] mat2 = new int[3][3];
    public int[][] mat1;

    public ReferenciaGenerator(int pageSize, int numRows, int numCols) {
        this.pageSize = pageSize;
        this.numRows = numRows;
        this.numCols = numCols;
        this.filterSize = 3;
        this.references = new ArrayList<>();
        this.matriz = new String[numRows][numCols];
        this.resultado = new String[numRows][numCols];
        this.mat3 = new int[numRows][numCols];
        this.mat1 = new int[numRows][numCols];
        generateReferences();
    }

    public void generateReferences() {
        int bytesPerInt = 4;
        int filterSize = this.filterSize * this.filterSize * bytesPerInt;
        int totalSize = filterSize + 2 * numRows * numCols * bytesPerInt;
        numPages = (int) Math.ceil((double) totalSize / pageSize);

        int pageIndex = 0;
        int offset = 0;

        // Generar referencias para el filtro
        for (int i = 0; i < this.filterSize; i++) {
            for (int j = 0; j < this.filterSize; j++) {
                String reference = pageIndex + "," + offset;
                filtro[i][j] = reference;
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
                String reference = pageIndex + "," + offset;
                matriz[i][j] = reference;
                references.add(reference);
                offset += bytesPerInt;
                if (offset >= pageSize) {
                    pageIndex++;
                    offset = 0;
                }
            }
        }

        // Generar referencias para la matriz de resultado
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                String reference = pageIndex + "," + offset;
                resultado[i][j] = reference;
                references.add(reference);
                offset += bytesPerInt;
                if (offset >= pageSize) {
                    pageIndex++;
                    offset = 0;
                }
            }
        }
        int numReferences = 19 * (numRows - 2) * (numCols - 2) + 2 * (numRows - 2) + 2 * (numCols - 2) + 4;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("referencias.txt"));

            writer.write("TP=" + pageSize + '\n');
            writer.write("NF=" + numRows + '\n');
            writer.write("NC=" + numCols + '\n');
            writer.write("NF_NC_Filtro=3" + '\n');
            writer.write("NR=" + numReferences + '\n');
            writer.write("NP=" + numPages + '\n');

            // Aplicar filtro y escribir en referencias.txt
            for (int i = 1; i < numRows - 1; i++) {
                for (int j = 1; j < numCols - 1; j++) {
                    int acum = 0;
                    for (int a = -1; a <= 1; a++) {
                        for (int b = -1; b <= 1; b++) {
                            int i2 = i + a;
                            int j2 = j + b;
                            int i3 = a + 1;
                            int j3 = b + 1;
                            acum += mat1[i2][j2] * mat2[i3][j3];

                            String[] mat1Values = matriz[i2][j2].split(",");
                            int pageIndexMat1 = Integer.parseInt(mat1Values[0]);
                            int offsetMat1 = Integer.parseInt(mat1Values[1]);
                            writer.write("M[" + i2 + "][" + j2 + "]," + pageIndexMat1 + "," + offsetMat1 + ",R\n");

                            // Se imprimen los valores de la matriz mat2
                            String[] mat2Values = filtro[i3][j3].split(",");
                            int pageIndexMat2 = Integer.parseInt(mat2Values[0]);
                            int offsetMat2 = Integer.parseInt(mat2Values[1]);
                            writer.write("F[" + i3 + "][" + j3 + "]," + pageIndexMat2 + "," + offsetMat2 + ",R\n");
                        }
                    }
                    if (acum >= 0 && acum <= 255) {
                        mat3[i][j] = acum;
                    } else if (acum < 0) {
                        mat3[i][j] = 0;
                    } else {
                        mat3[i][j] = 255;
                    }
                    String[] mat3Values = resultado[i][j].split(",");
                    int pageIndexMat3 = Integer.parseInt(mat3Values[0]);
                    int offsetMat3 = Integer.parseInt(mat3Values[1]);
                    writer.write("R[" + i + "][" + j + "]," + pageIndexMat3 + "," + offsetMat3 + ",W\n");
                }
            }
            // se asigna un valor predefinido a los bordes
            for (int i = 0; i < numCols; i++) {
                mat3[0][i] = 0;
                mat3[numRows - 1][i] = 255;
                String[] mat3Values = resultado[0][i].split(",");
                int pageIndexMat3 = Integer.parseInt(mat3Values[0]);
                int offsetMat3 = Integer.parseInt(mat3Values[1]);
                writer.write("R[0][" + i + "]," + pageIndexMat3 + "," + offsetMat3 + ",W\n");

                String[] mat3Values2 = resultado[numRows - 1][i].split(",");
                int pageIndexMat3_2 = Integer.parseInt(mat3Values2[0]);
                int offsetMat3_2 = Integer.parseInt(mat3Values2[1]);
                writer.write("R[" + (numRows - 1) + "][" + i + "]," + pageIndexMat3_2 + "," + offsetMat3_2 + ",W\n");
            }
            for (int i = 1; i < numRows - 1; i++) {
                mat3[i][0] = 0;
                mat3[i][numCols - 1] = 255;
                String[] mat3Values = resultado[i][0].split(",");
                int pageIndexMat3 = Integer.parseInt(mat3Values[0]);
                int offsetMat3 = Integer.parseInt(mat3Values[1]);
                writer.write("R[" + i + "][0]," + pageIndexMat3 + "," + offsetMat3 + ",W\n");

                String[] mat3Values2 = resultado[i][numCols - 1].split(",");
                int pageIndexMat3_2 = Integer.parseInt(mat3Values2[0]);
                int offsetMat3_2 = Integer.parseInt(mat3Values2[1]);
                writer.write("R[" + i + "][" + (numCols - 1) + "]," + pageIndexMat3_2 + "," + offsetMat3_2 + ",W\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

}
