package com.busap.vcs.util.page;

public class Page {
    //每页显示记录数
    private int pageSize;
    //总页数
    private int totalPage;
    //总记录数
    private int totalResult;
    //当前页
    private int currentPage;
    //当前记录起始行
    private int currentResult;

    /**
     *  获得记录分页总页数
     * @return 总页数
     */
    public int getTotalPage() {
        if (totalResult % pageSize == 0) {
            totalPage = totalResult / pageSize;
        } else {
            totalPage = totalResult / pageSize + 1;
        }
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public int getCurrentPage() {
        if (currentPage <= 0) {
            currentPage = 1;
        }
        if (currentPage > getTotalPage()) {
            currentPage = getTotalPage();
        }
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentResult() {
        currentResult = (getCurrentPage() - 1) * getPageSize();
        if (currentResult < 0) {
            currentResult = 0;
        }
        return currentResult;
    }

    public void setCurrentResult(int currentResult) {
        this.currentResult = currentResult;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
