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

    public KeyValue(Integer key, String Value) {
        this.key = key;
        this.value = Value;
    }

    @Override
    public String toString() {
        return value.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
