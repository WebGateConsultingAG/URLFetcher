/*
 * © Copyright WebGate Consulting AG, 2014
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

import static org.junit.Assert.*;

import org.junit.Test;

public class CommonWebSiteTest {

	@Test
	public void testGoogleWebSite() throws FetcherException {
		String url = "https://www.google.ch";
		PageAnalyse analyse = URLFetcher.analyseURL(url);
		assertNotNull(analyse);
	}
	@Test
	public void testYoutoubWebSite() throws FetcherException {
		String url = "https://www.youtube.com";
		PageAnalyse analyse =URLFetcher.analyseURL(url);
		assertNotNull(analyse);
	}
	@Test
	public void testGuedeByteWebSite() throws FetcherException {
		String url = "https://guedebyte.wordpress.com";
		PageAnalyse analyse =URLFetcher.analyseURL(url);
		assertNotNull(analyse);
	}
	@Test
	public void testNotExtistWebSite() {
		String url = "https://thisIsnotaURL";
		try {
		URLFetcher.analyseURL(url);
		} catch (FetcherException ex) {
			assertTrue(true);
			return;
		}
		assertTrue("Should throw an excpetion",false);

	}
}
