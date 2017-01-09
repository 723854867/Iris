package com.busap.vcs.base;


import java.io.Serializable;


@SuppressWarnings("unchecked")
public  class OrderByObject implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2131956636181342608L;

	public static enum OrderByType {
        
    	ASC(0,"升序"),
        
    	DESC(1,"降序");

        private Integer code;
        
        private String description;
        
        OrderByType(Integer code,String description){
        	this.code=code;
        	this.description=description;
        }

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
        
        
    }
    
    
    
    public OrderByObject(String rootName, String propertyName,Integer orderByType) {
		super();
		this.rootName = rootName;
		this.propertyName = propertyName;
		this.orderByType = orderByType;
	}

	private String rootName;
    
    private String propertyName;
    
    private Integer orderByType;

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Integer getOrderByType() {
		return orderByType;
	}

	public void setOrderByType(Integer orderByType) {
		this.orderByType = orderByType;
	}

    
}
