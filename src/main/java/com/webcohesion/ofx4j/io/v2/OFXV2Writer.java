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

package com.webcohesion.ofx4j.io.v2;

import com.webcohesion.ofx4j.OFXSettings;
import com.webcohesion.ofx4j.io.v1.OFXV1Writer;

import java.io.*;
import java.util.Map;

/**
 * OFX writer to XML, suitable for OFX version 2.0.
 *
 * @author Ryan Heaton
 */
public class OFXV2Writer extends OFXV1Writer {
  private OFXSettings ofxSettings;

  public OFXV2Writer(OutputStream out) {
    super(out);
    ofxSettings = OFXSettings.getInstance();
  }

  public OFXV2Writer(Writer writer) {
    super(writer);
    ofxSettings = OFXSettings.getInstance();
  }

  public OFXV2Writer(String filename) throws FileNotFoundException {
    super(filename);
    ofxSettings = OFXSettings.getInstance();
  }

  @Override
  protected OutputStreamWriter newWriter(OutputStream out) throws UnsupportedEncodingException {
    ofxSettings = OFXSettings.getInstance();
    return new OutputStreamWriter(out, ofxSettings.getEncoding());
  }

  @Override
  public void writeHeaders(Map<String, String> headers) throws IOException {
    ofxSettings = OFXSettings.getInstance();
    if (headersWritten) {
      throw new IllegalStateException("Headers have already been written!");
    }

    print("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
    if (ofxSettings.getWriteAttributesOnNewLine()) {
      println();
    }

    // write out the XML PI
    String security = headers.get("SECURITY");
    if (security == null) {
      security = "NONE";
    }
    String olduid = headers.get("OLDFILEUID");
    if (olduid == null) {
      olduid = "NONE";
    }
    // println(olduid);
    String uid = headers.get("NEWFILEUID");
    if (uid == null) {
      uid = "NONE";
    }

    print(String.format("<?OFX OFXHEADER=\"200\" VERSION=\"202\" SECURITY=\"%s\" OLDFILEUID=\"%s\" NEWFILEUID=\"%s\"?>",
        security, olduid, uid));
    this.headersWritten = true;
  }

  @Override
  public void writeElement(String name, String value) throws IOException {
    super.writeElement(name, value);
    print("</");
    print(name);
    print('>');
  }

  @Override
  public boolean isWriteAttributesOnNewLine() {
    return ofxSettings.getWriteAttributesOnNewLine();
  }
}
