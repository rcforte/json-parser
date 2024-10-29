package com.rcforte.parser.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rcforte.parser.json.TokenType.*;

public class Parser {
    private static String clear(String value) {
        value = value.strip();
        value = value.substring(1);
        value = value.substring(0, value.length() - 1);
        return value;
    }

    private final Scanner scanner;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    public Token nextToken() {
        return scanner.nextToken();
    }

    private Token currentToken() {
        return scanner.currentToken();
    }

    public Map<String, Object> parse() {
        var token = scanner.nextToken();
        if (token.type != OBJECT_START) {
            System.err.println("Expected end of file, found: " + token.value);
            System.exit(1);
        }

        var result = parseObject(token);

        token = nextToken();
        if (token.type != EOF) {
            System.err.println("Expected end of file, found: " + token.value);
            System.exit(1);
        }

        return result;
    }

    public Map<String, Object> parseObject(Token token) {
        var result = new HashMap<String, Object>();

        if (token.type == OBJECT_START) {
            var properties = parseProperties(token);
            for (var property : properties) {
                result.putAll(property);
            }
        }

        token = currentToken();
        if (token.type != OBJECT_END) {
            System.err.println("Expected object terminator, but found: " + token.value);
            System.exit(1);
        }

        return result;
    }

    private List<Map<String, Object>> parseProperties(Token token) {
        var properties = new ArrayList<Map<String, Object>>();

        do {
            token = nextToken();
            properties.add(parseProperty(token));
            token = nextToken();
        } while (token.type == PROPERTIES_SEPARATOR);

        return properties;
    }

    private Map<String, Object> parseProperty(Token token) {
        var property = new HashMap<String, Object>();

        // Make sure assumption is valid.
        if (token.type != STRING) {
            System.err.println("Expected string, but found: " + token.value);
            System.exit(1);
        }

        // Read property name.
        var name = clear(token.value);
        token = nextToken();
        if (token.type != PROPERTY_SEPARATOR) {
            System.err.println("Expected property separator, but found: " + token.value);
            System.exit(1);
        }

        // Read property value.
        var value = (Object) null;
        token = nextToken();
        if (token.type == STRING) {
            value = clear(token.value);
        } else if (token.type == NUMBER) {
            value = Integer.parseInt((String) token.value);
        } else if (token.type == OBJECT_START) {
            value = parseObject(token);
        } else if (token.type == ARRAY_START) {
            value = parseArray(token);
        } else {
            System.err.println("Expected string or number, but found: " + token.value);
            System.exit(1);
        }
        property.put(name, value);

        return property;
    }

    public List<Object> parseArray(Token token) {
        var result = new ArrayList<>();

        if (token.type == ARRAY_START) {
            var elements = parseElements();
            result.addAll(elements);
        }

        token = currentToken();
        if (token.type != ARRAY_END) {
            System.err.println("Expected object terminator, but found: " + token.value);
            System.exit(1);
        }

        return result;
    }

    public List<Object> parseElements() {
        var elements = new ArrayList<Object>();
        var token = (Token) null;

        do {
            token = nextToken();
            elements.add(parseElement(token));
            token = nextToken();
        } while (token.type == PROPERTIES_SEPARATOR);

        return elements;
    }

    public Object parseElement(Token token) {
        var element = (Object) null;
        if (token.type == STRING) {
            element = clear(token.value);
        } else if (token.type == NUMBER) {
            element = Integer.parseInt((String) token.value);
        } else if (token.type == OBJECT_START) {
            element = parseObject(token);
        } else {
            System.err.println("Expected string or number, but found: " + token.value);
            System.exit(1);
        }
        return element;
    }
}