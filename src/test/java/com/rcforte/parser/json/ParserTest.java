package com.rcforte.parser.json;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class ParserTest {
    @Test
    public void parse_JSON_String() {
        var json = """
                {
                    "name": "Rafael",
                    "age": 42,
                    "friend": {
                        "name": "John",
                        "age": 48
                    },
                    "list": [
                        1,
                        '2',
                        {'number': 3},
                        4
                    ]
                }
                """;

        var source = new Source(json);
        var scanner = new Scanner(source);
        var parser = new Parser(scanner);
        var object = parser.parse();

        assertEquals("Rafael", (String) object.get("name"));
        assertEquals(42, (Integer) object.get("age"));

        var map = new HashMap<String, Object>();
        map.put("name", "John");
        map.put("age", 48);
        assertEquals(map, object.get("friend"));

        var list = new ArrayList<Object>();
        list.add(1);
        list.add("2");
        var objElement = new HashMap<String, Object>();
        objElement.put("number", 3);
        list.add(objElement);
        list.add(4);
        assertIterableEquals(list, (List) object.get("list"));
    }
}