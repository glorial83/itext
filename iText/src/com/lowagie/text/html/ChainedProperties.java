package com.lowagie.text.html;
import java.util.ArrayList;
import java.util.HashMap;

public class ChainedProperties {

    private ArrayList chain = new ArrayList();


    public ChainedProperties() {
    }

    public ArrayList getList(){
        return chain;
    }

    public void add(String tag, HashMap attributes) {
        chain.add(new Object[] { tag, attributes });
    }

    public void remove(String tag) {
        for (int k = chain.size() - 1; k >= 0; --k) {
            if (tag.equals(((Object[]) chain.get(k))[0])) {
                chain.remove(k);
                return;
            }
        }
    }

    /** Returns the attributes for a given tag in the chain. */
    public HashMap getAttributes(String tag){
        for (int k = chain.size() - 1; k >= 0; --k) {
            if (tag.equals(((Object[]) chain.get(k))[0])) {
                return (HashMap)((Object[]) chain.get(k))[1];
            }
        }
        return null;
    }

    /** Returns the last tag in the chain. */
    public String getLastTag(){
        return (String)((Object[]) chain.get(chain.size()-1))[0];
    }

    public String getProperty(String key) {
		for (int k = chain.size() - 1; k >= 0; --k) {
			Object obj[] = (Object[]) chain.get(k);
			HashMap prop = (HashMap) obj[1];
			String ret = (String) prop.get(key);
			if (ret != null)
				return ret;
		}
		return null;
	}
}
