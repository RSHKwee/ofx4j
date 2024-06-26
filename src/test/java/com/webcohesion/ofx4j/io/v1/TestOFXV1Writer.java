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

import junit.framework.TestCase;

import java.io.StringWriter;

import com.webcohesion.ofx4j.OFXSettings;

/**
 * @author Ryan Heaton
 */
public class TestOFXV1Writer extends TestCase {

  /**
   * tests character escaping
   */
  public void testCharacterEscaping() throws Exception {
    OFXSettings OFXSetting = OFXSettings.getInstance();
    OFXSetting.setWriteAttributesOnNewLine(false);

    StringWriter value = new StringWriter();
    OFXV1Writer writer = new OFXV1Writer(value);
    writer.writeElement("NAME", "&<>");
    writer.close();
    assertEquals("<NAME>&amp;&lt;&gt;", value.toString());
  }

}
