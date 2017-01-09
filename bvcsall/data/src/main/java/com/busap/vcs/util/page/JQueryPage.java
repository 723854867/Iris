package com.busap.vcs.util.page;

/**
 * JQuery 翻页查询对象
 * User: huoshanwei
 * Date: 15-10-23
 * Time: 下午4:56
 *
 */
public class JQueryPage extends Page {

    //每页显示的记录数
    private Integer rows;

    //当前第几页
    private Integer page;

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
