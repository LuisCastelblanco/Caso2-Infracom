import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ReportGenerator {
    private MemorySimulator memorySimulator;
    private String outputFile;

    public ReportGenerator(MemorySimulator memorySimulator, String outputFile) {
        this.memorySimulator = memorySimulator;
        this.outputFile = outputFile;
    }

    public void generateReport() {
        try (FileWriter writer = new FileWriter(outputFile)) {
            // Escribir el encabezado del informe
            writer.write("Informe de Simulación de Memoria Virtual\n");
            writer.write("----------------------------------------\n\n");

            // Escribir la descripción del algoritmo de generación de referencias
            writer.write("Algoritmo de Generación de Referencias:\n");
            writer.write("El algoritmo de generación de referencias se basa en el método de multiplicación de matrices.\n");
            writer.write("Se genera una referencia por cada elemento de la matriz de datos y el filtro durante el proceso de multiplicación.\n");
            writer.write("Las referencias incluyen la matriz, la posición, el número de página, el desplazamiento y la acción (lectura o escritura).\n\n");

            // Escribir las estructuras de datos utilizadas
            writer.write("Estructuras de Datos Utilizadas:\n");
            writer.write("- Tabla de Páginas: Mapeo entre números de página y marcos de página.\n");
            writer.write("- Lista de Marcos de Página: Representa los marcos de página en memoria.\n");
            writer.write("- Cola de Referencias: Almacena las referencias de página generadas.\n\n");

            // Escribir el esquema de sincronización utilizado
            writer.write("Esquema de Sincronización:\n");
            writer.write("Se utilizaron métodos synchronized en las operaciones críticas de actualización de la tabla de páginas y los marcos de página.\n");
            writer.write("Esto asegura que solo un hilo pueda acceder y modificar las estructuras de datos compartidas a la vez, evitando condiciones de carrera.\n\n");

            // Escribir la tabla de datos recopilados
            writer.write("Tabla de Datos Recopilados:\n");
            writer.write(String.format("%-20s %-20s %-20s\n", "Tamaño de Página", "Frames Asignados", "Porcentaje de Aciertos"));
            Map<Integer, Map<Integer, Double>> hitPercentages = memorySimulator.getHitPercentages();
            for (Map.Entry<Integer, Map<Integer, Double>> entry : hitPercentages.entrySet()) {
                int pageSize = entry.getKey();
                Map<Integer, Double> frameHitPercentages = entry.getValue();
                for (Map.Entry<Integer, Double> frameEntry : frameHitPercentages.entrySet()) {
                    int frames = frameEntry.getKey();
                    double hitPercentage = frameEntry.getValue();
                    writer.write(String.format("%-20d %-20d %-20.2f%%\n", pageSize, frames, hitPercentage));
                }
            }

            // Escribir las gráficas de resultados (puedes adaptar esta parte según tus necesidades)
            writer.write("\nGráficas de Resultados:\n");
            writer.write("(Incluye aquí las gráficas generadas)\n");

            // Escribir la interpretación de los resultados
            writer.write("\nInterpretación de los Resultados:\n");
            writer.write("(Incluye aquí tu interpretación de los resultados obtenidos)\n");

            System.out.println("Informe generado exitosamente: " + outputFile);
        } catch (IOException e) {
            System.out.println("Error al generar el informe: " + e.getMessage());
        }
    }
}