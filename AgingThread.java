public class AgingThread extends Thread {
    private MemorySimulator memorySimulator;
    private boolean running;

    public AgingThread(MemorySimulator memorySimulator) {
        this.memorySimulator = memorySimulator;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Ejecutar el algoritmo de actualización del bit de referencia
                memorySimulator.updateReferenceBits();

                // Esperar 4 milisegundos antes de la próxima actualización
                Thread.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        running = false;
    }
}