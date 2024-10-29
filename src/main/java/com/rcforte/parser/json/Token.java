package com.rcforte.parser.json;

public class Token {
  public String value;
  public TokenType type;

  public Token(String value, TokenType type) {
    this.value = value;
    this.type = type;
  }

  public Token() {
    this(null, null);
  }
}
