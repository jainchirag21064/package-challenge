package com.mobiquity.exception;

public class APIException extends RuntimeException {

  public APIException(String message, Exception e) {
    super(message, e);
  }

  public APIException(String message) {
    super(message);
  }
}
