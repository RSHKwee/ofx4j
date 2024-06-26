/*
 * Copyright 2008 Web Cohesion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webcohesion.ofx4j.io.v1;

import com.webcohesion.ofx4j.OFXSettings;
import com.webcohesion.ofx4j.io.OFXWriter;

import java.util.Map;
import java.io.*;

/**
 * OFX writer to SGML, suitable for OFX versions < 2.0.
 *
 * @author Ryan Heaton
 */
public class OFXV1Writer implements OFXWriter {
  private OFXSettings ofxSettings = OFXSettings.getInstance();

  private static final String LINE_SEPARATOR = "\r\n";
  protected boolean headersWritten = false;
  protected final Writer writer;
  private boolean writeAttributesOnNewLine = ofxSettings.getWriteAttributesOnNewLine();
  private String filename = "";

  public OFXV1Writer(String filename) throws FileNotFoundException {
    this.filename = filename;
    OutputStream out = new FileOutputStream(filename);
    try {
      this.writer = newWriter(out);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public OFXV1Writer(OutputStream out) {
    try {
      this.writer = newWriter(out);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public OFXV1Writer(Writer writer) {
    this.writer = writer;
  }

  protected OutputStreamWriter newWriter(OutputStream out) throws UnsupportedEncodingException {
    return new OutputStreamWriter(out, ofxSettings.getEncoding());
  }

  @Override
  public void writeHeaders(Map<String, String> headers) throws IOException {
    if (headersWritten) {
      throw new IllegalStateException("Headers have already been written!");
    }

    // write out the 1.0 headers
    println("OFXHEADER:100");
    println("DATA:OFXSGML");
    println("VERSION:102");

    print("SECURITY:");
    String security = headers.get("SECURITY");
    if (security == null) {
      security = "NONE";
    }
    println(security);
    println("ENCODING:USASCII"); // too many ofx v1 servers don't read unicode...
    println("CHARSET:1252"); // windows-compatible.
    println("COMPRESSION:NONE");
    print("OLDFILEUID:");
    String olduid = headers.get("OLDFILEUID");
    if (olduid == null) {
      olduid = "NONE";
    }
    println(olduid);
    print("NEWFILEUID:");
    String uid = headers.get("NEWFILEUID");
    if (uid == null) {
      uid = "NONE";
    }
    println(uid);

    this.headersWritten = true;
  }

  @Override
  public void writeStartAggregate(String aggregateName) throws IOException {
    if (isWriteAttributesOnNewLine()) {
      println();
      print(ofxSettings.getIndentSpaces());
    }

    ofxSettings.incrIndent();
    print('<');
    print(aggregateName);
    print('>');
  }

  @Override
  public void writeElement(String name, String value) throws IOException {
    ofxSettings = OFXSettings.getInstance();
    if ((value == null) || ("".equals(value))) {
      throw new IllegalArgumentException(
          "Illegal element value for element '" + name + "' (value must not be null or empty).");
    }

    // todo: optimize performance of the character escaping
    if (value.indexOf('&') >= 0) {
      value = value.replaceAll("\\&", "&amp;");
    }

    if (value.indexOf('<') >= 0) {
      value = value.replaceAll("<", "&lt;");
    }

    if (value.indexOf('>') >= 0) {
      value = value.replaceAll(">", "&gt;");
    }

    if (isWriteAttributesOnNewLine()) {
      println();
      print(ofxSettings.getIndentSpaces());
    }
    print('<');
    print(name);
    print('>');
    print(value);
  }

  @Override
  public void writeEndAggregate(String aggregateName) throws IOException {
    ofxSettings.decrIndent();
    if (isWriteAttributesOnNewLine()) {
      println();
      print(ofxSettings.getIndentSpaces());
    }
    print("</");
    print(aggregateName);
    print('>');
  }

  public boolean isWriteAttributesOnNewLine() {
    return writeAttributesOnNewLine;
  }

  public void setWriteAttributesOnNewLine(boolean writeAttributesOnNewLine) {
    this.writeAttributesOnNewLine = writeAttributesOnNewLine;
  }

  @Override
  public void close() throws IOException {
    flush();
    this.writer.close();
  }

  public void flush() throws IOException {
    this.writer.flush();
  }

  protected void println(String line) throws IOException {
    print(line);
    println();
  }

  protected String getFilename() {
    return this.filename;
  }

  protected void println() throws IOException {
    this.writer.write(LINE_SEPARATOR);
  }

  protected void print(String line) throws IOException {
    this.writer.write(line == null ? "null" : line);
  }

  protected void print(char ch) throws IOException {
    this.writer.write(ch);
  }
}
