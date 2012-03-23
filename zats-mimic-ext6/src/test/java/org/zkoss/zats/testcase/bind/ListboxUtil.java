package org.zkoss.zats.testcase.bind;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

public class ListboxUtil {
	public static long getSelectedIndex(ComponentAgent listbox){
		return listbox.as(Listbox.class).getSelectedIndex();
	}
	
	public static boolean isSelected(ComponentAgent listitem){
		return listitem.as(Listitem.class).isSelected();
	}
	
	public static long[] getSelectedIndexs(ComponentAgent listbox){
		//Welcome To Facebook....selected is always false in listitem
		//so this api is useless...
		List<Long> indexs = new ArrayList<Long>();
		List<ComponentAgent> outeritems = listbox.getChildren();//include header
		
		long index = 0;
		for(ComponentAgent w:outeritems){
			if(w.is(Listitem.class)){
				if(isSelected(w)){
					indexs.add(index);
				}
				index++;
			}
		}
		long[] ix = new long[indexs.size()];
		for(int i=0;i<ix.length;i++){
			ix[i] = indexs.get(i);
		}
		return ix;
	}
}
