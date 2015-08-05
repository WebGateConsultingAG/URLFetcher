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

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class URLFetcher {

	public static PageAnalyse analyseURL(String url) throws FetcherException {
		return analyseURL(url, 200);
	}

	public static PageAnalyse analyseURL(String url, int maxPictureHeight) throws FetcherException {
		PageAnalyse analyse = new PageAnalyse(url);
		try {
			HttpClient httpClient = new DefaultHttpClient();
			((DefaultHttpClient) httpClient).setRedirectStrategy(new DefaultRedirectStrategy());
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Content-Type", "text/html; charset=utf-8");
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				try {
					parseContent(maxPictureHeight, analyse, entity.getContent());
				} catch (IllegalStateException e) {
					throw new FetcherException(e);
				} catch (SAXException e) {
					throw new FetcherException(e);
				} finally {
					httpClient.getConnectionManager().shutdown();
				}

			} else {
				throw new FetcherException("Response from WebSite is :" + statusCode);
			}

		} catch (Exception e) {
			throw new FetcherException(e);
		}
		return analyse;
	}

	public static void parseContent(int maxPictureHeight, PageAnalyse analyse, InputStream is) throws SAXNotRecognizedException, SAXNotSupportedException, SAXException, IOException {
		Document doc;
		DOMParser dpHTML = new DOMParser();
		dpHTML.setProperty("http://cyberneko.org/html/properties/default-encoding", "utf-8");
		dpHTML.parse(new InputSource(is));
		doc = dpHTML.getDocument();

		NodeList ndlMet = doc.getElementsByTagName("meta");
		NodeList ndlTitle = doc.getElementsByTagName("title");
		NodeList ndlImage = doc.getElementsByTagName("img");

		check4OpenGraphTags(ndlMet, analyse);
		checkDescription(analyse, ndlMet);
		checkTitle(analyse, ndlTitle);
		checkImage(analyse, ndlImage, maxPictureHeight);
	}

	public static void checkImage(PageAnalyse analyse, NodeList ndlImage, int maxPictureHeigh) {
		for (int nCounter = 0; nCounter < ndlImage.getLength(); nCounter++) {
			Element elCurrent = (Element) ndlImage.item(nCounter);
			if (elCurrent.hasAttribute("src")) {
				String strImage = elCurrent.getAttribute("src");
				if (ndlImage.getLength() > 20 && elCurrent.hasAttribute("height")) {
					String strHeight = elCurrent.getAttribute("height");
					strHeight = strHeight.replace("px", "");
					try {
						int nHeight = Integer.parseInt(strHeight);
						if (nHeight > maxPictureHeigh) {
							strImage = null;
						}
					} catch (Exception e) {
						// TODO:handle exception
					}
				}
				if (strImage != null) {
					analyse.addImageURL(strImage);

				}
			}

		}
	}

	public static void checkTitle(PageAnalyse analyse, NodeList ndlTitle) {
		if ("".equals(analyse.getTitle())) {
			analyse.setTitle(((Element) ndlTitle.item(0)).getFirstChild().getNodeValue());
		}
	}

	public static void checkDescription(PageAnalyse analyse, NodeList ndlMet) {
		if ("".equals(analyse.getDescription())) {
			for (int nCounter = 0; nCounter < ndlMet.getLength(); nCounter++) {
				Element elCurrent = (Element) ndlMet.item(nCounter);
				if ("description".equalsIgnoreCase(elCurrent.getAttribute("name"))) {
					if (elCurrent.hasAttribute("content")) {
						analyse.setDescription(elCurrent.getAttribute("content"));
						nCounter = ndlMet.getLength();
					}
				}
			}
		}
	}

	private static void check4OpenGraphTags(NodeList ndlMeta, PageAnalyse analyse) {
		for (int nCounter = 0; nCounter < ndlMeta.getLength(); nCounter++) {
			Element elMeta = (Element) ndlMeta.item(nCounter);
			// Test if property is available
			if (elMeta.hasAttribute("property")) {
				String strProperty = elMeta.getAttribute("property");
				// CHECK if we have a OpenGraphProperty
				if (strProperty.toLowerCase().startsWith("og:")) {
					analyse.addOpenGraph(strProperty, elMeta.getAttribute("content"));
				}
				if ("og:image".equalsIgnoreCase(strProperty)) {
					analyse.addImageURL(elMeta.getAttribute("content"));
				}
				if ("og:title".equalsIgnoreCase(strProperty)) {
					analyse.setTitle(elMeta.getAttribute("content"));
				}
				if ("og:description".equalsIgnoreCase(strProperty)) {
					analyse.setDescription(elMeta.getAttribute("content"));
				}
			}
		}
	}

}
