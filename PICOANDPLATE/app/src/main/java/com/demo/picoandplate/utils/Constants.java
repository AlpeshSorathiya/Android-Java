package com.demo.picoandplate.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASorathiya
 */
public class Constants {

    public static final Map<Integer, String> RULES_DATA =
            Collections.unmodifiableMap(new HashMap<Integer, String>() {{
                put(1, "Monday");
                put(2, "Monday");
                put(3, "Tuesday");
                put(4, "Tuesday");
                put(5, "Wednesday");
                put(6, "Wednesday");
                put(7, "Thursday");
                put(8, "Thursday");
                put(9, "Friday");
                put(0, "Friday");
            }});
}
