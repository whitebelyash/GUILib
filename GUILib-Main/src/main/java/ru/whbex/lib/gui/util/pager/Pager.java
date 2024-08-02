package ru.whbex.lib.gui.util.pager;

import java.util.ArrayList;
import java.util.List;

/**
 * List paginator
 * @param <T> list entry type
 */
public class Pager<T> {

    private final int size;
    private final int pageSize;
    private final List<T> list;
    public Pager(List<T> list, int pageSize){
        this.size = list.size();
        this.list = list;
        this.pageSize = pageSize;
    }
    public List<T> getPage(int page) {
        if(page < 1)
            throw new IllegalArgumentException("Invalid page number");
        if(page > getPagesAmount())
            page = 1; // or there's a better way to handle this?
        int startIndex = (page-1)*pageSize;
        int endIndex = startIndex + pageSize;
        if(startIndex > list.size() || endIndex > list.size())
            throw new IllegalStateException(String.format("Start/End index exceeds list size!" +
                                                            " (size: %d, startIndex: %d, endIndex: %d",
                                                            list.size(), startIndex, endIndex));
        return list.subList(startIndex, endIndex);
    }
    public int getPageCount() {
        return list.size() % pageSize == 0 ? list.size() / pageSize : list.size() / pageSize + 1;
    }
    public int getPagesAmount(){
        return list.size() % pageSize == 0 ? list.size() / pageSize : list.size() / pageSize + 1;
    }
}
