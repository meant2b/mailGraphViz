import java.util.ArrayList;
import java.util.HashMap;


public class StaticConnectionsMap {

    private static HashMap<String, ArrayList<String>> connections;
    private static HashMap<String,String> emailName;

    public static HashMap<String,ArrayList<String>> getConnections(){
       if(connections==null){
           connections  = new HashMap<>();
       }
        return connections;
    }

    public static void setConnections(HashMap<String, ArrayList<String>> connections1){
        connections = connections1;
    }

    public void emptyConnections(){
        connections = new HashMap<>();
    }
}
