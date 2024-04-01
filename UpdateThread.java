public class UpdateThread extends Thread {
    private MemorySimulator memorySimulator;
    private boolean running;

    public UpdateThread(MemorySimulator memorySimulator) {
        this.memorySimulator = memorySimulator;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Actualizar el estado de la tabla de páginas y los marcos de página en RAM
                memorySimulator.updatePageTable();

                // Esperar 1 milisegundo antes de la próxima actualización
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        running = false;
    }
}