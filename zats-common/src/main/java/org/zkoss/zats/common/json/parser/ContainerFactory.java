/*
 *    Copyright 2011 JSON-SMART authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zkoss.zats.common.json.parser;

import java.util.List;
import java.util.Map;

import org.zkoss.zats.common.json.JSONArray;
import org.zkoss.zats.common.json.JSONObject;
import org.zkoss.zats.common.json.parser.ContainerFactory;

/**
 * Container factory for creating containers for JSON object and JSON array.
 *
 * @author FangYidong<fangyidong@yahoo.com.cn>
 * @since 1.1.0
 */
public class ContainerFactory {
	public final static ContainerFactory FACTORY = new ContainerFactory();
	
	/**
	 * @return A Map instance to store JSON object, or null if you want to use JSONObject.
	 */
	public Map<String,Object> createObjectContainer() {
		return new JSONObject();
	}
	
	/**
	 * @return A List instance to store JSON array, or null if you want to use JSONArray. 
	 */
	public List<Object> creatArrayContainer() {
		return new JSONArray();
	}
}
