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

package eu.woolplatform.editor.script.model;


import java.io.FileWriter;
import java.io.IOException;

/**
 * A WoolScriptNodeBody contains the body of an (un-parsed) wool node. It is a
 * simple container object for the {@code script} String and a series of convenience
 * methods for storing the results of (shallow) parsing this body.
 *
 * @author Harm op den Akker (Innovation Sprint)
 */
public class WoolScriptNodeBody {

	private String script;

	// ----- Constructors:

	/**
	 * Creates an instance of an empty {@link WoolScriptNodeBody}.
	 */
	public WoolScriptNodeBody() {
		script = "";
	}

	/**
	 * Creates an instance of a {@link WoolScriptNodeBody} instantiated with the given
	 * {@code script}-String that contains the wool script for this node.
	 * @param script the wool script that makes up the body of this wool node.
	 */
	public WoolScriptNodeBody(String script) {
		this.script = script;
	}

	/**
	 * Creates an instance of a {@link WoolScriptNodeBody} as a copy of the given {@code other}
	 * {@link WoolScriptNodeBody}.
	 * @param other
	 */
	public WoolScriptNodeBody(WoolScriptNodeBody other) {
		this.script = other.script;
	}

	// ----- Getters:

	public String getScript() {
		return script;
	}

	// ----- Setters:

	public void setScript(String script) {
		this.script = script;
	}

	// ----- Functions:

	public String toString() {
		return script;
	}

	public void writeBody(FileWriter fileWriter) throws IOException {
		fileWriter.write(script);
	}

}
