package org.androidtown.seterm;

import java.util.HashMap;
import java.util.Map;

/**
 * class that define user information<br/>
 * @author SeProject
 * @date 2020-06-21
 * @version 0.0.1
 */
public class User {
    public String username;
    public String order1;
    public String order2;
    public String order3;
    public String order4;
    public String time;
    public String sum;

    /**
     * if the constructor is empty, don't do anything
     */
    public User() {

    }

    /**
     * if constructor has each information, set user information to new user information
     * @param username user's name
     * @param order1 order information of americano
     * @param order2 order information of cafelatte
     * @param order3 order information of cappuccino
     * @param order4 order information of camomile
     * @param time reservation tiem
     * @param sum total price
     */
    public User(String username, String order1, String order2, String order3, String order4, String time, String sum) {
        this.username = username;
        this.order1 = order1;
        this.order2 = order2;
        this.order3 = order3;
        this.order4 = order4;
        this.time = time;
        this.sum = sum;
    }

    /**
     * method that make user information a Map
     * @return User Mapping data
     */
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
