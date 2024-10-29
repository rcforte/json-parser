package com.rcforte.parser.json;

class Source {
    private final String content;
    private int index = -1;
    private char currentChar;

    public Source(String content) {
        this.content = content;
    }

    public char currentChar() {
        return index == -1 ? nextChar() : currentChar;
    }

    public char nextChar() {
        if (index + 1 < content.length()) {
            currentChar = content.charAt(++index);
        } else {
            currentChar = (char) -1;
        }

        while (currentChar != (char) -1 && Character.isWhitespace(currentChar)) {
            currentChar = nextChar();
        }

        return currentChar;
    }
}
