import java.util.HashMap;
import java.util.Map;

public class PageTable {
    private Map<Integer, PageFrame> pageTable;

    public PageTable() {
        this.pageTable = new HashMap<>();
    }

    public void addEntry(int pageNumber, PageFrame pageFrame) {
        pageTable.put(pageNumber, pageFrame);
    }

    public void removeEntry(int pageNumber) {
        pageTable.remove(pageNumber);
    }

    public boolean isPageInMemory(int pageNumber) {
        return pageTable.containsKey(pageNumber);
    }

    public PageFrame getPageFrame(int pageNumber) {
        return pageTable.get(pageNumber);
    }

    public void updatePageFrame(int pageNumber, PageFrame pageFrame) {
        pageTable.put(pageNumber, pageFrame);
    }

    public void clear() {
        pageTable.clear();
    }

    public int getSize() {
        return pageTable.size();
    }

    public Map<Integer, PageFrame> getPageTable() {
        return pageTable;
    }
}