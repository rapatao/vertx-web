package io.vertx.ext.web.api.contract.openapi3;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.ext.web.api.validation.impl.SingleValueParameterTypeValidator;

import java.util.HashMap;
import java.util.Map;

public class OpenAPI3Options {

  public static class ParameterValidator<T> {
    private String name;
    private SingleValueParameterTypeValidator validator;

    ParameterValidator(String name, SingleValueParameterTypeValidator<T> validator) {
      this.name = name;
      this.validator = validator;
    }
    public String getName() {
      return this.name;
    }

    public SingleValueParameterTypeValidator<T> getValidator() {
      return this.validator;
    }
  }

  private Map<String, ParameterValidator> customValidators = new HashMap<>();

  @Fluent
  public <T> OpenAPI3Options addSchemaCustomValidator(final String type,
                                                      final String format,
                                                      final SingleValueParameterTypeValidator<T> validator) {
    customValidators.put(type, new ParameterValidator<>(format, validator));
    return this;
  }

  @Fluent
  public OpenAPI3Options addSchemaCustomValidator(final Map<String, ParameterValidator> customValidators) {
    if (customValidators != null) {
      this.customValidators.putAll(customValidators);
    }
    return this;
  }

  public Map<String, ParameterValidator> getCustomValidators() {
    return this.customValidators;
  }
}
