package org.zkoss.zk.zats.example.service;

import java.util.Date;

/**
 * 
 * @author robbiecheng
 *
 */

public class Task {
	private String id;
	private String name;
	private int priority;
	private Date date;
	
	public Task(){}
	public Task(String id,String name,int priority,Date date){
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	//important
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}
	
	//important
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Task)) {
			return false;
		}
		final Task o = (Task) other;
		return o.id == this.id || (o.id != null && o.id.equals(this.id));
	}
}
