package org.Iris.app.jilu.storage.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ztree node domain
 * Created by guest on 2017/1/3.
 *
 * @see <a href="http://www.treejs.cn/v3/api.php">http://www.treejs.cn/v3/api.php</a>
 */
public class ZtreeNode<T> implements Serializable {

    private static final long serialVersionUID = 6883895989226428719L;
    /**
     * true 表示节点的输入框被勾选
     * false 表示节点的输入框未勾选
     */
    private boolean checked;
//    /**
//     * 节点的子节点数据集合。
//     */
//    private List<ZtreeNode<T>> children = new ArrayList<>();
    /**
     * true 表示此节点的 checkbox / radio 被禁用。
     * false 表示此节点的 checkbox / radio 可以使用。
     */
    private boolean chkDisabled;
    /**
     * 最简单的 click 事件操作。相当于 onclick="..." 的内容。
     * 标准 javascript 语法， 例如：alert("test"); 等
     */
    private String click;
    /**
     * 节点自定义图标的 URL 路径。
     */
    private String icon;
    /**
     * 父节点自定义折叠时图标的 URL 路径。
     */
    private String iconClose;
    /**
     * 父节点自定义展开时图标的 URL 路径。
     */
    private String iconOpen;
    /**
     * 节点自定义图标的 className
     */
    private String iconSkin;
    /**
     * 节点名称
     */
    private String name;
    /**
     * rue 表示此节点不显示 checkbox / radio，不影响勾选的关联关系，不影响父节点的半选状态。
     * false 表示节点具有正常的勾选功能
     */
    private boolean nocheck;
    private int pId;
    /**
     * 记录 treeNode 节点的 展开 / 折叠 状态。
     * true 表示节点为 展开 状态
     * false 表示节点为 折叠 状态
     */
    private boolean open;
    private boolean isParent = false;
    /**
     * 设置点击节点后在何处打开 url。
     * 同超链接 target 属性: "_blank", "_self" 或 其他指定窗口名称
     * 省略此属性，则默认为 "_blank"
     */
    private String target;
    /**
     * 节点链接的目标 URL
     */
    private String url;
    private int id;

    private T obj;


	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean isChecked() {
        return checked;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChkDisabled() {
        return chkDisabled;
    }

    public void setChkDisabled(boolean chkDisabled) {
        this.chkDisabled = chkDisabled;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconClose() {
        return iconClose;
    }

    public void setIconClose(String iconClose) {
        this.iconClose = iconClose;
    }

    public String getIconOpen() {
        return iconOpen;
    }

    public void setIconOpen(String iconOpen) {
        this.iconOpen = iconOpen;
    }

    public String getIconSkin() {
        return iconSkin;
    }

    public void setIconSkin(String iconSkin) {
        this.iconSkin = iconSkin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNocheck() {
        return nocheck;
    }

    public void setNocheck(boolean nocheck) {
        this.nocheck = nocheck;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
