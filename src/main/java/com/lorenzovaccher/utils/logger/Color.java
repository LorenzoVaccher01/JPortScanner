/**
 * @author Lorenzo Vaccher
 * Copyright (c) 2021 Lorenzo Vaccher.
 */

package com.lorenzovaccher.utils.logger;

public enum Color {
  RED("\033[0;31m"),
  GREEN("\033[0;32m"),
  YELLOW("\033[0;33m"),
  BLUE("\033[0;34m"),
  WHITE("\033[0;37m"),
  MAGENTA("\033[0;35m"),
  CYAN("\033[0;36m"),
  RED_BOLD("\033[1;31m"),
  GREEN_BOLD("\033[1;32m"),
  BLUE_BOLD("\033[1;34m"),
  WHITE_BOLD("\033[1;37m"),
  MAGENTA_BOLD("\033[1;35m"),
  CYAN_BOLD("\033[1;36m"),
  RESET("\u001B[0m");

  private String code;

  Color(String code) {
    this.code = code;
  }

  public String getColor(){
    return this.code;
  }
}