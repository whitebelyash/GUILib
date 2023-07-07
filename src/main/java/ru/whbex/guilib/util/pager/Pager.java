package ru.whbex.guilib.util.pager;

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
    private final int pageCount;
    public Pager(List<T> list, int pageSize){
        this.size = list.size();
        this.list = list;
        this.pageSize = pageSize;
        if(list.size() % pageSize == 0)
            pageCount = list.size() / pageSize;
        else
            pageCount = list.size() / pageSize + 1;
    }
    public List<T> getPage(int page) throws PagerException {
        if(page < 1)
            throw new PagerException(PagerException.Reason.INVALID_NUMBER);
        if(page > pageCount)
            throw new PagerException(PagerException.Reason.MAX_PAGE_EXCEEDED);
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
        return pageCount;
    }
}
