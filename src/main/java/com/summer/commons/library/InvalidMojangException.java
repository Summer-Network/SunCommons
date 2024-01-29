package com.summer.commons.library;

public class InvalidMojangException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidMojangException(String msg) {
    super(msg);
  }
}
