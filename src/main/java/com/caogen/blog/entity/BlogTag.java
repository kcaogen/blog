package com.caogen.blog.entity;

@SuppressWarnings("serial")
public class BlogTag implements java.io.Serializable {
	
	private int tagId;					//主键ID
	
	private String tagName;				//标签名称

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}