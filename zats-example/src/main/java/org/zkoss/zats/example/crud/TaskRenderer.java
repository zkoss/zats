package org.zkoss.zats.example.crud;

import java.text.SimpleDateFormat;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class TaskRenderer implements ListitemRenderer<Task>{

	public static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

	public void render(Listitem listitem, Task t,int index ){
		
		Listcell nameCell = new Listcell();
		nameCell.setLabel(t.getName());

		Listcell priorityCell = new Listcell();
		priorityCell.setLabel(Integer.toString(t.getPriority()));
		
		Listcell dateCell = new Listcell();
		dateCell.setLabel(dateFormatter.format(t.getDate()));
		
		listitem.appendChild(nameCell);
		listitem.appendChild(priorityCell);
		listitem.appendChild(dateCell);
		
	}
}

