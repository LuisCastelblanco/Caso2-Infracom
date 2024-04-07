import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Menú:");
            System.out.println("1. Generar referencias");
            System.out.println("2. Calcular datos");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    generateReferences(scanner);
                    break;
                case 2:
                    calculateData(scanner);
                    break;
                case 3:
                    exit = true;
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
                    break;
            }

            System.out.println();
        }
    }

    private static void generateReferences(Scanner scanner) {
        System.out.print("Ingrese el tamaño de página: ");
        int pageSize = scanner.nextInt();
        System.out.print("Ingrese el número de filas de la matriz: ");
        int numRows = scanner.nextInt();
        System.out.print("Ingrese el número de columnas de la matriz: ");
        int numCols = scanner.nextInt();

        /**
        ReferenceGenerator generator = new ReferenceGenerator(pageSize, numRows, numCols);
        **/
        ReferenciaGenerator generator = new ReferenciaGenerator(pageSize, numRows, numCols);
        generator.generateReferences();
        String fileName = "referencias.txt";

       

        System.out.println("Referencias generadas y guardadas en el archivo: " + fileName);
    }

    private static void calculateData(Scanner scanner) {
        System.out.print("Ingrese el número de marcos de página: ");
        int numPageFrames = scanner.nextInt();
        System.out.print("Ingrese el nombre del archivo de referencias: ");
        String fileName = scanner.next();

        MemorySimulator simulator = new MemorySimulator(numPageFrames, fileName);
        simulator.simulate();

        System.out.println("Resultados de la simulación:");
        System.out.println("Número de fallos de página: " + simulator.getPageFaults());
        System.out.println("Numero de hits: " + simulator.hits());
        System.out.println("Porcentaje de hits: " + simulator.getHitPercentage() + "%");

    }
}