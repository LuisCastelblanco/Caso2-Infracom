// Asegúrate de que esta clase exista y realice su función correctamente
class ReferenceBitUpdaterThread extends Thread {
    private MemorySimulator simulator;

    public ReferenceBitUpdaterThread(MemorySimulator simulator) {
        this.simulator = simulator;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(4000); // Esperar 4 segundos
                simulator.updateReferenceBits();
            } catch (InterruptedException e) {
                break; // Permitir terminar el thread
            }
        }
    }
}
