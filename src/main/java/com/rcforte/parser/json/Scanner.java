package com.rcforte.parser.json;

public class Scanner {
  private final Source source;
  private Token currentToken;

  public Scanner(Source source) {
    this.source = source;
  }

  public Token nextToken() {
    var token = (Token) null;
    var ch = source.currentChar();
    if(ch == (char) -1) {
      token = new Token("<EOF>", TokenType.EOF);
    } else if(ch == '{') {
      token = new Token(Character.toString(ch), TokenType.OBJECT_START);
      source.nextChar();
    } else if(ch == '}') {
      token = new Token(Character.toString(ch), TokenType.OBJECT_END);
      source.nextChar();
    } else if(ch == '[') {
      token = new Token(Character.toString(ch), TokenType.ARRAY_START);
      source.nextChar();
    } else if(ch == ']') {
      token = new Token(Character.toString(ch), TokenType.ARRAY_END);
      source.nextChar();
    } else if(isQuote(ch)) {
      var str = new StringBuilder();
      str.append(ch);
      do {
        ch = source.nextChar();
        str.append(ch);
      } while(!isQuote(ch));

      token = new Token(str.toString(), TokenType.STRING);
      source.nextChar();
    } else if(Character.isDigit(ch)) {
      var str = new StringBuilder();
      do {
        str.append(ch);
        ch = source.nextChar();
      } while(Character.isDigit(ch));

      token = new Token(str.toString(), TokenType.NUMBER);
    } else if(ch == ':') {
      token = new Token(Character.toString(ch), TokenType.PROPERTY_SEPARATOR);
      source.nextChar();
    } else if(ch == ',') {
      token = new Token(Character.toString(ch), TokenType.PROPERTIES_SEPARATOR);
      source.nextChar();
    }

    currentToken = token;

    return currentToken;
  }

  public Token currentToken() {
    return currentToken;
  }

  public boolean isQuote(char ch) {
    return ch == '\'' || ch == '"';
  }
}
