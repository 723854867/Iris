package org.Iris.app.jilu.common;

import java.util.ArrayList;
import java.util.List;

import org.Iris.app.jilu.storage.domain.SysPermission;
import org.Iris.app.jilu.storage.domain.ZtreeNode;

/**
 * 操作权限树的工具类
 * Created by guest on 2016/7/5.
 */
public class TreeHelper {
    /**
     * 待处理节点list
     */
    private List<ZtreeNode<SysPermission>> tempNodeList = new ArrayList<>();
    /**
     * 实体list
     */
    private List<SysPermission> dataList;
    /**
     * 需要勾选的实体
     */
    private List<SysPermission> drawDataList;

    public TreeHelper() {
    }

    public TreeHelper(List<SysPermission> dataList) {
        this.dataList = dataList;
    }

    public TreeHelper(List<SysPermission> dataList, List<SysPermission> drawDataList) {
        this.dataList = dataList;
        this.drawDataList = drawDataList;
    }

    public void togetherTwoList() {
        for (SysPermission sys : dataList) {
        	ZtreeNode<SysPermission> tree = changeToZtreeNode(sys);
            for (int i = 0; i < drawDataList.size(); i++) {
                if (sys.getPermissionId()==(drawDataList.get(i).getPermissionId())) {
                    tree.setChecked(true);
                }
            }
            tree.setOpen(true);
            tempNodeList.add(tree);
        }
    }
    
    public void oneList(){
    	 for (SysPermission sys : dataList) {
         	ZtreeNode<SysPermission> tree = changeToZtreeNode(sys);
            tree.setOpen(true);
            tempNodeList.add(tree);
         }
    }


    /**
     * 数据库实体转换成节点实体
     *
     * @param sysPermission 权限实体
     * @return 节点实体
     */
    private ZtreeNode<SysPermission> changeToZtreeNode(SysPermission sysPermission) {
    	ZtreeNode<SysPermission> node = new ZtreeNode<>();
        node.setId(sysPermission.getPermissionId());
        node.setpId(sysPermission.getPid());
        node.setName(sysPermission.getPermissionName());
        return node;
    }


    public List<SysPermission> getDataList() {
        return dataList;
    }

    public void setDataList(List<SysPermission> dataList) {
        this.dataList = dataList;
    }

	public List<ZtreeNode<SysPermission>> getTempNodeList() {
		return tempNodeList;
	}

	public void setTempNodeList(List<ZtreeNode<SysPermission>> tempNodeList) {
		this.tempNodeList = tempNodeList;
	}


}
