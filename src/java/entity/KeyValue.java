/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author ahas36
 */
public class KeyValue {
    Integer key;
    String value;
    Integer time;
    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String Value) {
        this.value = Value;
    }

    public KeyValue(Integer key, String Value,Integer time) {
        this.key = key;
        this.value = Value;
        this.time=time;
    }

    @Override
    public String toString() {
        return value.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
    
    
    
}
