package edu.umass.ckc.wo.content;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jnewman
 * Date: 10/15/14
 * Time: 6:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Binding  {
    private Map<String, String> vars;
    private int position;

    public Binding() {
        this.vars = new HashMap<String, String>();
    }

    public Binding(Map<String, String> vars) {
        this.vars = vars;
    }

    public void addKVPair(String key, String val) {
        vars.put(key, val);
    }

    public String get(String var) {
        return vars.get(var);
    }

    public Map<String, String> getMap() {
        return vars;
    }

    public int size() {
        return vars.size();
    }

    public Set<String> keySet() {
        return vars.keySet();
    }

    public boolean containsKey(String key) {
        return vars.containsKey(key);
    }

    @Override
    public String toString() {
        return getJSON(new JSONObject()).toString();
    }

    public JSONObject getJSON(JSONObject jo) {
        for(Map.Entry<String, String> entry : vars.entrySet()){
            jo.element(entry.getKey(), entry.getValue());
        }
        return jo;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Binding copy () {
        Binding c  = new Binding();
        c.vars = new HashMap<String,String>(this.vars); // a shallow copy of this.vars
        c.position = this.position;
        return c;
    }

    @Override
    public boolean equals(Object bind) {
        Binding b;
        if (bind instanceof Binding) {
            b = (Binding) bind;
        }
        else {
            return false;
        }
        if (this.vars == null || b == null || b.getMap() == null) {
            return false;
        }
        if (b.size() != this.size()) {
            return false;
        }
        for (String key : b.keySet()) {
           if (!(this.containsKey(key) && b.get(key).equals(this.get(key)))) {
               return false;
           }
        }
        return true;
    }


}
