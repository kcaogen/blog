package com.caogen.blog.entity;

@SuppressWarnings("serial")
public class Type implements java.io.Serializable {
	
	private int typeId;					//主键ID
	
	private String typeName;			//类型名称

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "Type{" +
				"typeId=" + typeId +
				", typeName='" + typeName + '\'' +
				'}';
	}
}
