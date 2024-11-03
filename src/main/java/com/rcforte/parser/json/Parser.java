package com.rcforte.parser.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rcforte.parser.json.TokenType.*;

public class Parser {
  public static Object parse(Class<?> clazz, Map<String, Object> map) throws Exception {
    if(clazz == null)
      throw new IllegalArgumentException("clazz cannot be null");
    if(map == null)
      throw new IllegalArgumentException("map cannot be null");

    var obj = clazz.newInstance();
    var fields = clazz.getDeclaredFields();

    for(var field : fields) {
      if(!field.isAnnotationPresent(JsonField.class))
        continue;

      var annotation = field.getAnnotationsByType(JsonField.class)[0];
      field.setAccessible(true);

      if(!field.getType().isAnnotationPresent(JsonClass.class))
        field.set(obj, map.get(annotation.mappedTo()));
      else {
        var innerObj = (Map) map.get(annotation.mappedTo());
        if(innerObj == null)
          continue;

        var fieldObj = parse(field.getType(), innerObj);
        field.set(obj, fieldObj);
      }
    }

    return(obj);
  }

  public static Object parse(Class<?> clazz, String json) throws Exception {
    if(clazz == null)
      throw new IllegalArgumentException("clazz cannot be null");
    if(json == null)
      throw new IllegalArgumentException("json cannot be null");
    if(!clazz.isAnnotationPresent(JsonClass.class))
      return null;

    var source = new Source(json);
    var scanner = new Scanner(source);
    var parser = new Parser(scanner);
    var mapObj = parser.parse();

    return parse(clazz, mapObj);
  }

  public static void quit(String message) {
    System.err.println(message);
    System.exit(1);
  }

  /**
   * Change a string like " 'myString' " to "myString".
   */
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
    if(token.type != OBJECT_START)
      quit("Expected end of file, found: " + token.value);

    var result = parseObject(token);

    token = nextToken();
    if(token.type != EOF)
      quit("Expected end of file, found: " + token.value);

    return result;
  }

  public Map<String, Object> parseObject(Token token) {
    var result = new HashMap<String, Object>();

    if(token.type == OBJECT_START) {
      var properties = parseProperties(token);
      for(var property : properties)
        result.putAll(property);
    }

    token = currentToken();
    if(token.type != OBJECT_END)
      quit("Expected object terminator, but found: " + token.value);

    return result;
  }

  private List<Map<String, Object>> parseProperties(Token token) {
    var properties = new ArrayList<Map<String, Object>>();

    do {
      token = nextToken();
      properties.add(parseProperty(token));
      token = nextToken();
    } while(token.type == PROPERTIES_SEPARATOR);

    return properties;
  }

  private Map<String, Object> parseProperty(Token token) {
    var property = new HashMap<String, Object>();

    // Validate assumptions.
    if(token.type != STRING) {
      quit("Expected string, but found: " + token.value);
    }

    // Read property name.
    var name = clear(token.value);
    token = nextToken();
    if(token.type != PROPERTY_SEPARATOR) {
      quit("Expected property separator, but found: " + token.value);
    }

    // Read property value.
    var value = (Object) null;
    token = nextToken();
    if(token.type == STRING) {
      value = clear(token.value);
    } else if(token.type == NUMBER) {
      value = Integer.parseInt((String) token.value);
    } else if(token.type == OBJECT_START) {
      value = parseObject(token);
    } else if(token.type == ARRAY_START) {
      value = parseArray(token);
    } else {
      quit("Expected string or number, but found: " + token.value);
    }

    property.put(name, value);

    return property;
  }

  public List<Object> parseArray(Token token) {
    var result = new ArrayList<>();

    if(token.type == ARRAY_START) {
      var elements = parseElements();
      result.addAll(elements);
    }

    token = currentToken();
    if(token.type != ARRAY_END)
      quit("Expected object terminator, but found: " + token.value);

    return result;
  }

  public List<Object> parseElements() {
    var elements = new ArrayList<Object>();
    var token = (Token) null;

    do {
      token = nextToken();
      elements.add(parseElement(token));
      token = nextToken();
    } while(token.type == PROPERTIES_SEPARATOR);

    return elements;
  }

  public Object parseElement(Token token) {
    var element = (Object) null;
    if(token.type == STRING) {
      element = clear(token.value);
    } else if(token.type == NUMBER) {
      element = Integer.parseInt((String) token.value);
    } else if(token.type == OBJECT_START) {
      element = parseObject(token);
    } else {
      quit("Expected string or number, but found: " + token.value);
    }
    return element;
  }

}