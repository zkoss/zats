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

import java.io.IOException;
import java.io.Reader;

/**
 * Parser for JSON text. Please note that JSONParser is NOT thread-safe.
 * 
 * @author Uriel Chemouni <uchemouni@gmail.com>
 * @since 1.1.0
 */
public class JSONParser extends JSONParserStream {

	protected static boolean[] stopArray = new boolean[126];
	protected static boolean[] stopKey = new boolean[126];
	protected static boolean[] stopValue = new boolean[126];
	protected static boolean[] stopX = new boolean[126];
	static {
		stopKey[':'] = true;
		stopValue[','] = stopValue['}'] = true;
		stopArray[','] = stopArray[']'] = true;
	}

	public Object parse(String in) throws IOException, ParseException {
		return parse(new FStringReader(in), ContainerFactory.FACTORY);
	}

	public Object parse(String in, ContainerFactory containerFactory) throws ParseException {
		try {
			return parse(new FStringReader(in), containerFactory);
		} catch (IOException e) {
			// cant not be throws
			return null;
		}
	}

	/**
	 * Non Syncronized limited StringReader
	 */
	private static class FStringReader extends Reader {
		private String str;
		private int length;
		private int next = 0;

		public FStringReader(String s) {
			this.str = s;
			this.length = s.length();
		}

		public int read() throws IOException {
			if (next >= length)
				return -1;
			return str.charAt(next++);
		}

		public int read(char cbuf[], int off, int len) throws IOException {
			if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
				throw new IndexOutOfBoundsException();
			} else if (len == 0) {
				return 0;
			}
			if (next >= length)
				return -1;
			int n = Math.min(length - next, len);
			str.getChars(next, next + n, cbuf, off);
			next += n;
			return n;
		}

		public boolean ready() throws IOException {
			return true;
		}

		public boolean markSupported() {
			return false;
		}

		public void close() {
		}
	}
}
