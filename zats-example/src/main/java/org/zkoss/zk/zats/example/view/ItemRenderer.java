package org.zkoss.zk.zats.example.view;

import java.text.DecimalFormat;

import org.zkoss.zk.zats.example.service.Item;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class ItemRenderer implements ListitemRenderer{

	public static DecimalFormat priceFormatter = new DecimalFormat("$ ###,###,###,##0.00");

	public void render(Listitem listitem, Object data){
		Item i = (Item)data;
		Listcell nameCell = new Listcell();
		nameCell.setLabel(i.getName());
		Listcell priceCell = new Listcell();
		priceCell.setLabel(priceFormatter.format(i.getPrice()));
		Listcell quantityCell = new Listcell();
		quantityCell.setLabel(Integer.toString(i.getQuantity()));
		if (i.getQuantity()<3){
			quantityCell.setSclass("red");
		}
		
		listitem.appendChild(nameCell);
		listitem.appendChild(priceCell);
		listitem.appendChild(quantityCell);
		
	}
}

