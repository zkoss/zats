/* UploadComposer.java

	Purpose:
		
	Description:
		
	History:
		2013/4/24 Created by Hawk

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zats.essentials;

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

/**
 * @author Hawk
 *
 */
public class UploadComposer extends SelectorComposer<Component>{

	@Wire
	private Vbox results;
	
	@Listen("onUpload = #btn, #uploadBox")
	public void upload(UploadEvent event) {
		results.getChildren().clear();
		org.zkoss.util.media.Media[] medias = event.getMedias();
		if(medias != null) {
			for(int i = 0; i < medias.length; ++i)
				show(i, medias[i]);
		}
	}
	
	@Listen("onClick = #label1")
	public void upload(){
		Fileupload.get();
	}
	
	@Listen("onClick = #label2")
	public void upload2(){
		Fileupload.get(3);
	}
	
	@Listen("onClick = #clean")
	public void clean(){
		results.getChildren().clear();
	}
	
	private void createLabel(Component container, String key, String value) {
		Hbox box = new Hbox();
		box.setParent(container);
		new Label(key + ": ").setParent(box);
		Label label = new Label(value != null ? value : "NULL");
		label.setClass(key);
		label.setParent(box);
	}
	
	private void show(int index, org.zkoss.util.media.Media media) {
		System.out.println("received: " + media.getName());

		Vbox box = new Vbox();
		box.setId("file" + index);
		box.setStyle("border: 1px solid black; padding: 3px;");
		box.setParent(results);

		createLabel(box, "name", media.getName());
		createLabel(box, "contentType", media.getContentType());
		createLabel(box, "format", media.getFormat());
		createLabel(box, "binary", media.isBinary() ? new java.math.BigInteger(1, media.getByteData()).toString(16).toUpperCase() : "");
		createLabel(box, "text", media.isBinary() ? "" : media.getStringData());
		if(media instanceof AImage) {
			AImage image = (AImage)media;
			createLabel(box, "width", image.getWidth() + "px");
			createLabel(box, "height", image.getHeight() + "px");
		}
		else {
			createLabel(box, "width", "");
			createLabel(box, "height", "");
		}
	}
}
