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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

public class PageAnalyseTest {


	@Test
	public void testTitle() throws Exception  {
		PageAnalyse analyse = analyseFile();
		assertNotNull(analyse);
		assertEquals("guedebyte", analyse.getTitle());
	}

	@Test
	public void testDescription() throws Exception  {
		PageAnalyse analyse = analyseFile();
		assertNotNull(analyse);
		assertEquals("Das Leben und die Gedanken von Guede", analyse.getDescription());
	}

	@Test
	public void testImages() throws Exception  {
		PageAnalyse analyse = analyseFile();
		assertNotNull(analyse);
		assertEquals(48, analyse.getImagesUrls().size());
	}


	@Test
	public void testOpenGraphTags() throws Exception  {
		PageAnalyse analyse = analyseFile();
		assertNotNull(analyse);
		assertEquals(9, analyse.getOpenGraph().size());
	}

	public PageAnalyse analyseFile() throws Exception {
		InputStream is = getURLforTestFile("guedebyte.wordpress.com.htm");
		PageAnalyse analyse = new PageAnalyse("testURL");
		URLFetcher.parseContent(200, analyse, is);
		return analyse;
	}

	protected InputStream getURLforTestFile(String testFile) {
		InputStream is = getClass().getResourceAsStream("/" + testFile);
		assertNotNull("Testfile is missing", is);
		return is;
	}

}
