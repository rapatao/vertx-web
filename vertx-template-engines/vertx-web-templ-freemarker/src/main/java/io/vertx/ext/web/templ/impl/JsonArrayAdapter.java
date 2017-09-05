/*
 * Copyright 2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.ext.web.templ.impl;

import freemarker.template.AdapterTemplateModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;
import freemarker.template.WrappingTemplateModel;
import io.vertx.core.json.JsonArray;

/**
 * @author Thomas Segismont
 */
public class JsonArrayAdapter extends WrappingTemplateModel implements TemplateSequenceModel, AdapterTemplateModel {

  private final JsonArray jsonArray;

  public JsonArrayAdapter(JsonArray jsonArray, ObjectWrapper ow) {
    super(ow);
    this.jsonArray = jsonArray;
  }

  @Override
  public int size() throws TemplateModelException {
    return jsonArray.size();
  }

  @Override
  public TemplateModel get(int index) throws TemplateModelException {
    return index >= 0 && index < jsonArray.size() ? wrap(jsonArray.getValue(index)) : null;
  }

  @Override
  public Object getAdaptedObject(Class hint) {
    return jsonArray;
  }

}
