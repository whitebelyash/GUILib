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
        List<T> ret = new ArrayList<>();
        for(int pointer = 0; pointer < pageSize; pointer++){
            int step = (page-1)*pageSize;
            int pos = pointer+step;
            if(pos >= size)
                break;
            ret.add(list.get(pos));
        }
        return ret;
    }
    public int getPageCount() {
        return list.size() % pageSize == 0 ? list.size() / pageSize : list.size() / pageSize + 1;
    }
    public int getPagesAmount(){
        return list.size() % pageSize == 0 ? list.size() / pageSize : list.size() / pageSize + 1;
    }
}
