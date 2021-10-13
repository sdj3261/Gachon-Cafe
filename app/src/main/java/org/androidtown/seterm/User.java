package org.androidtown.seterm;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String username;
    public String order1;
    public String order2;
    public String order3;
    public String order4;
    public String time;
    public String sum;

    public User() {

    }

    public User(String username, String order1, String order2, String order3, String order4, String time, String sum) {
        this.username = username;
        this.order1 = order1;
        this.order2 = order2;
        this.order3 = order3;
        this.order4 = order4;
        this.time = time;
        this.sum = sum;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", username);
        result.put("order1", order1);
        result.put("order2", order2);
        result.put("order3", order3);
        result.put("order4", order4);
        result.put("time", time);
        result.put("sum", sum);

        return result;
    }

}
