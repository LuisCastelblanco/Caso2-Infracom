import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MemorySimulator {
    private int numPageFrames;
    private String referenceFile;
    private List<PageFrame> pageFrames;
    private PageTable pageTable;
    private int pageFaults;
    private int hits;
    private int totalReferences;

    public MemorySimulator(int numPageFrames, String referenceFile) {
        this.numPageFrames = numPageFrames;
        this.referenceFile = referenceFile;
        this.pageFrames = new ArrayList<>(numPageFrames);
        this.pageTable = new PageTable();
        this.pageFaults = 0;
        this.hits = 0;
        this.totalReferences = 0;
    }
    public synchronized void updatePageTable() {
        // Recorrer todos los marcos de página en memoria
        for (PageFrame pageFrame : pageFrames) {
            int pageNumber = pageFrame.getPageNumber();
            
            // Actualizar el bit de referencia del marco de página
            pageFrame.setReferenceBit(true);
            
            // Actualizar el tiempo de último acceso del marco de página
            pageFrame.setLastAccessTime(System.currentTimeMillis());
            
            // Actualizar la entrada correspondiente en la tabla de páginas
            pageTable.updatePageFrame(pageNumber, pageFrame);
        }
    }
    public synchronized void updateReferenceBits() {
        // Recorrer todos los marcos de página en memoria
        for (PageFrame pageFrame : pageFrames) {
            // Obtener el valor actual del bit de referencia
            boolean referenceBit = pageFrame.isReferenceBit();
    
            // Desplazar el bit de referencia hacia la derecha
            int agingValue = pageFrame.getAgingValue();
            agingValue = (agingValue >> 1) | (referenceBit ? 0x80000000 : 0);
            pageFrame.setAgingValue(agingValue);
    
            // Reiniciar el bit de referencia a false
            pageFrame.setReferenceBit(false);
        }
    }
    


    

    public void simulate() {
        try (BufferedReader reader = new BufferedReader(new FileReader(referenceFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TP=") || line.startsWith("NF=") || line.startsWith("NC=") ||
                        line.startsWith("NF_NC_Filtro=") || line.startsWith("NR=") || line.startsWith("NP=")) {
                    continue; // Saltar líneas de encabezado
                }
    
                String[] parts = line.split(",");
                int pageNumber = Integer.parseInt(parts[1]);
                char action = parts[3].charAt(0);
    
                totalReferences++;
    
                if (pageTable.isPageInMemory(pageNumber)) {
                    // Acierto (hit)
                    hits++;
                } else {
                    // Fallo de página (miss)
                    pageFaults++;
    
                    if (pageFrames.size() < numPageFrames) {
                        // Hay marcos de página disponibles, cargar la página en un marco libre
                        PageFrame pageFrame = new PageFrame(pageNumber);
                        pageFrames.add(pageFrame);
                        pageTable.addEntry(pageNumber, pageFrame);
                    } else {
                        // No hay marcos de página disponibles, aplicar algoritmo de reemplazo
                        PageFrame victimFrame = selectVictimFrame();
                        pageTable.removeEntry(victimFrame.getPageNumber());
                        victimFrame.setPageNumber(pageNumber);
                        pageTable.addEntry(pageNumber, victimFrame);
                    }
                }
    
                // Actualizar los bits de referencia y modificación según la acción
                PageFrame pageFrame = pageTable.getPageFrame(pageNumber);
                if (action == 'R') {
                    pageFrame.setReferenceBit(true);
                } else if (action == 'W') {
                    pageFrame.setReferenceBit(true);
                    pageFrame.setModifiedBit(true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PageFrame selectVictimFrame() {
        List<PageFrame> class0 = new ArrayList<>();
        List<PageFrame> class1 = new ArrayList<>();
        List<PageFrame> class2 = new ArrayList<>();
        List<PageFrame> class3 = new ArrayList<>();

        for (PageFrame frame : pageFrames) {
            boolean referenced = frame.isReferenceBit();
            boolean modified = frame.isModifiedBit();

            if (!referenced && !modified) {
                class0.add(frame);
            } else if (!referenced && modified) {
                class1.add(frame);
            } else if (referenced && !modified) {
                class2.add(frame);
            } else if (referenced && modified) {
                class3.add(frame);
            }
        }

        if (!class0.isEmpty()) {
            return getRandomPage(class0);
        } else if (!class1.isEmpty()) {
            return getRandomPage(class1);
        } else if (!class2.isEmpty()) {
            return getRandomPage(class2);
        } else if (!class3.isEmpty()) {
            return getRandomPage(class3);
        }

        // Si no hay páginas en ninguna clase, seleccionar una página aleatoria
        return pageFrames.get(new Random().nextInt(pageFrames.size()));
    }

    private PageFrame getRandomPage(List<PageFrame> frames) {
        int randomIndex = new Random().nextInt(frames.size());
        return frames.get(randomIndex);
    }

    public int getPageFaults() {
        return pageFaults;
    }

    public double getHitPercentage() {
        return (double) hits / totalReferences * 100;
    }
    public Map<Integer, Map<Integer, Double>> getHitPercentages() {
    Map<Integer, Map<Integer, Double>> hitPercentages = new HashMap<>();

    // Obtener los diferentes tamaños de página utilizados en la simulación
    Set<Integer> pageSizes = new HashSet<>();
    pageSizes.add(numPageFrames); // Suponiendo que el tamaño de página es igual al número de marcos de página

    // Calcular el porcentaje de aciertos para cada tamaño de página y número de frames
    for (int pageSize : pageSizes) {
        Map<Integer, Double> frameHitPercentages = new HashMap<>();
        for (int frames = 1; frames <= numPageFrames; frames++) {
            int hits = 0;
            int misses = 0;

            // Simular la ejecución con el tamaño de página y número de frames actual
            MemorySimulator simulator = new MemorySimulator(frames, referenceFile);
            simulator.simulate();

            // Obtener los resultados de la simulación
            hits = simulator.hits;
            misses = simulator.pageFaults;

            // Calcular el porcentaje de aciertos
            double hitPercentage = (double) hits / (hits + misses) * 100;
            frameHitPercentages.put(frames, hitPercentage);
        }
        hitPercentages.put(pageSize, frameHitPercentages);
    }

    return hitPercentages;
}
}