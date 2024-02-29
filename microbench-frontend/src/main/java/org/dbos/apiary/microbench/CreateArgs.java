package org.dbos.apiary.microbench;

public class CreateArgs {
    public void setNumEntries(int numEntries) {
        this.numEntries = numEntries;
    }

    public int getNumEntries() {
        return numEntries;
    }

    public void setEntrySize(int entrySize) {
        this.entrySize = entrySize;
    }

    public int getEntrySize() {
        return entrySize;
    }

    private int numEntries;
    private int entrySize;

}
