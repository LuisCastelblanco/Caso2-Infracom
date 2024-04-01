public class PageFrame {
    private int pageNumber;
    private boolean referenceBit;
    private boolean modifiedBit;
    private long lastAccessTime;
    private int agingValue;

    public PageFrame(int pageNumber) {
        this.pageNumber = pageNumber;
        this.referenceBit = false;
        this.modifiedBit = false;
        this.lastAccessTime = 0;
        this.agingValue = 0;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isReferenceBit() {
        return referenceBit;
    }

    public void setReferenceBit(boolean referenceBit) {
        this.referenceBit = referenceBit;
    }

    public boolean isModifiedBit() {
        return modifiedBit;
    }

    public void setModifiedBit(boolean modifiedBit) {
        this.modifiedBit = modifiedBit;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
    public int getAgingValue() {
        return agingValue;
    }

    public void setAgingValue(int agingValue) {
        this.agingValue = agingValue;
    }

    public void incrementLastAccessTime() {
        lastAccessTime++;
    }

    public void resetReferenceBit() {
        referenceBit = false;
    }

    public void resetModifiedBit() {
        modifiedBit = false;
    }
}