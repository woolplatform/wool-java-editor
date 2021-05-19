/*
 * Copyright 2019-2021 WOOL Foundation.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package eu.woolplatform.editor.script.parser;

import eu.woolplatform.editor.script.model.WoolScript;
import eu.woolplatform.utils.exception.ParseException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link WoolScriptParserResult} object contains the results of parsing a .wool file, including
 * the resulting {@link WoolScript} and a list of {@link ParseException}s.
 *
 * @author Dennis Hofs (Roessingh Research and Development)
 * @author Harm op den Akker (Innovation Sprint)
 */
public class WoolScriptParserResult {

	private WoolScript woolScript;
	private List<ParseException> parseErrors;

	/**
	 * Creates an instance of an empty {@link WoolScriptParserResult} object.
	 */
	public WoolScriptParserResult() {
		parseErrors = new ArrayList<ParseException>();
	}

	// ----- Getters

	/**
	 * Returns the {@link WoolScript} that is part of this {@link WoolScriptParserResult}.
	 * @return the {@link WoolScript} that is part of this {@link WoolScriptParserResult}.
	 */
	public WoolScript getWoolScript() {
		return woolScript;
	}

	/**
	 * Returns a {@link List} of {@link ParseException}s that have occurred during
	 * the parsing of the .wool file.
	 * @return a {@link List} of {@link ParseException}s.
	 */
	public List<ParseException> getParseErrors() {
		return parseErrors;
	}

	// ----- Setters

	/**
	 * Sets the {@link WoolScript} that is part of this {@link WoolScriptParserResult}.
	 * @param woolScript the {@link WoolScript} that is part of this {@link WoolScriptParserResult}.
	 */
	public void setWoolScript(WoolScript woolScript) {
		this.woolScript = woolScript;
	}
}
