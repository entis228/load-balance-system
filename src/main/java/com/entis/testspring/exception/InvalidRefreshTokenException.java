package com.entis.testspring.exception;

public class InvalidRefreshTokenException extends Exception {

  public InvalidRefreshTokenException() {
    super();
  }

  public InvalidRefreshTokenException(Throwable cause) {
    super(cause);
  }

}
