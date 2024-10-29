package com.rcforte.parser.json;

enum TokenType {
    OBJECT_START,
    OBJECT_END,

    ARRAY_START,
    ARRAY_END,

    PROPERTY_SEPARATOR,
    PROPERTIES_SEPARATOR,

    STRING,
    NUMBER,

    EOF,
}
