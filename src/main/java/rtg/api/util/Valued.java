package rtg.api.util;

public class Valued<Type>  {
    public Type item;
    public double value;
    
    public Valued(Type theItem, double theValue) {
        item = theItem;
        value = theValue;
    }
    public double value() {return value;}
    public Type object() {return item;}

    public String toString() {
        return item.toString() + " " + value;
    }
    
}

