/*
 * © Copyright WebGate Consulting AG, 2015
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package biz.webgate.tools.urlfetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PageAnalyse {

	private final String url;
	private final String documentBase;
	private final HashMap<String, String> openGraph = new HashMap<String, String>();
	private String title;
	private String description;
	private final Set<String> images = new HashSet<String>();

	public PageAnalyse(String url) {
		this.url = url;
		String baseURL = url;
		if (baseURL.lastIndexOf("/") > 8) {
			baseURL = baseURL.substring(0, baseURL.lastIndexOf("/"));
		}
		this.documentBase = baseURL;
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDocumentBase() {
		return documentBase;
	}

	public HashMap<String, String> getOpenGraph() {
		return openGraph;
	}

	public void addOpenGraph(String key, String value) {
		openGraph.put(key, value);
	}

	public void addImageURL(String image) {
		String imageAdd = checkIMAGEURL(image,documentBase);
		images.add(imageAdd);
	}

	private String checkIMAGEURL(String strImage, String strBaseURL) {
		String strRC = strImage;
		if (strRC.startsWith("//")) {
			strRC = "http:" + strRC;
		}
		if (!strRC.toLowerCase().startsWith("http://")) {
			strRC = strBaseURL + "/" + strRC;
		}

		return strRC;
	}
	public List<String> getImagesUrls() {
		return new ArrayList<String>(images);
	}
}
