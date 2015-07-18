import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HelloWorld extends JFrame
{

    /**
     *
     */
    private static final long serialVersionUID = -2707712944901661771L;

    public HelloWorld()
    {
        super("Hello, World!");

        mxGraph graph = new mxGraph();

        Object parent = graph.getDefaultParent();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //HashMap<String,ArrayList<String>> connections = StaticConnectionsMap.getConnections();
        MakeRespMatrix matrix = new MakeRespMatrix("C:\\Users\\Toreador\\Documents\\emls");
        HashMap<String,ArrayList<String>> connections = matrix.getConnections();
        graph.getModel().beginUpdate();

        int[] center = new int[]{(int)screenSize.getWidth()/2,(int)screenSize.getHeight()/2};
        try
        {
            Object[] senders = connections.keySet().toArray();
            HashMap<String,Object> labVertex = new HashMap<>();
            ArrayList<String> allEmails = matrix.getAllEmails();
            for(int i=0;i<allEmails.size();i++){
                int[] coords;
                if(i==0){
                    coords=center;
                }

                if(i%2==0){
                    coords= new int[]{center[0] + (i * 50)+50, center[1] + (i * 50)-30};
                }
                 else if(i%3==0){
                    coords= new int[]{center[0] - (i * 50)+10, center[1] + (i * 50)-10};
                } else if(i%5==0){
                    coords= new int[]{center[0] + (i * 50)+10, center[1] - (i * 50)-10};
                }
                else {
                    coords= new int[]{center[0]- (i * 80), center[1] };
                }

                Object vertex = graph.insertVertex(parent,null,allEmails.get(i)+"\r\n"+i,coords[0],coords[1],80,80);
                labVertex.put(allEmails.get(i),vertex);
            }

            Iterator iterator = connections.keySet().iterator();
            while(iterator.hasNext()){
                  String sender = (String) iterator.next();
                Object sendV = labVertex.get(sender);
                for (int j=0;j<connections.get(sender).size();j++){
                    Object recV = labVertex.get(connections.get(sender).get(j));
                    graph.insertEdge(parent,null,"sends to",sendV,recV);

                }
            }

            /*
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,
                    30);
            Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,
                    80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);*/
        } finally
        {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }

    public static void main(String[] args)
    {
        HelloWorld frame = new HelloWorld();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);
    }

}
