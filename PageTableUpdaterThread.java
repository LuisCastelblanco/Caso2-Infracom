public class PageTableUpdaterThread extends Thread {
    private MemorySimulator memorySimulator;

    public PageTableUpdaterThread(MemorySimulator memorySimulator) {
        this.memorySimulator = memorySimulator;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1); // Esperar 1 milisegundo
                memorySimulator.updatePageTable();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}