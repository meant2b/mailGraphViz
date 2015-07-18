import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import javax.mail.*;
import javax.mail.internet.MimeMessage;

public class MakeRespMatrix {

    private HashMap<String, ArrayList<String>> connections = StaticConnectionsMap.getConnections();
    private ArrayList<String> allEmails;

    public void addConnections(Eml eml){

        Set<String> mails = this.connections.keySet();
        MimeMessage message = eml.getMessage();
        try {

           Address[] getFrom = message.getFrom();
            Address[] recipients = message.getAllRecipients();
            ArrayList<String> recEmails = new ArrayList<>();
            for (int j=0;j<recipients.length;j++){
                recEmails.add((nameEmail(recipients[j])[1]));
                if(!allEmails.contains(nameEmail(recipients[j])[1])){
                    allEmails.add(nameEmail(recipients[j])[1]);
                }
            }
            ArrayList<String> from = new ArrayList<>();
            for (int i=0;i<getFrom.length;i++) {
                String emailFrom = nameEmail(getFrom[i])[1];
                if(!allEmails.contains(emailFrom)){
                    allEmails.add(emailFrom);
                }
                if(!mails.contains(emailFrom)){
                    connections.put(emailFrom,recEmails);
                }
                else{
                    //ArrayList<String> alreadyIn = connections.get(emailFrom);
                    for(int j=0;j<recEmails.size();j++){
                        if(!connections.get(emailFrom).contains(recEmails.get(j))){
                            connections.get(emailFrom).add(recEmails.get(j));
                        }
                    }

                }
            }


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    MakeRespMatrix(String path){
        File emlPath = new File(path);
        allEmails = new ArrayList<>();
        if(emlPath.isDirectory()){
        for(int i=0;i<emlPath.list().length;i++){
            String path1 = emlPath+File.separator+ emlPath.list()[i];
            Eml eml = new Eml(path1);
            addConnections(eml);
        }}
        else{
            Eml eml = new Eml(path);
            addConnections(eml);
        }
        setConnections();
    }

    public ArrayList<String> getAllEmails(){
        return allEmails;
    }

    public void setConnections(){
        StaticConnectionsMap.setConnections(connections);
    }

    public HashMap<String,ArrayList<String>> getConnections(){
        return StaticConnectionsMap.getConnections();
    }

    private static String[] nameEmail(Address address){
        String addressS = address.toString();
        System.out.println("Address:"+addressS);
        if(addressS.contains("<")){
        int c = addressS.indexOf("<");
        String name = addressS.substring(0, c - 1);
        String email = addressS.substring(c+1,addressS.length()-1);
        return new String[]{name,email};}
        else if(addressS.matches("^(?!.*?\\s).*?@.*?\\..*?")){
                return new String[]{"",addressS};
        }else {
            return new String[]{addressS,"error parsing"};
        }

    }
}

class Eml extends File {
    private MimeMessage message;
    public Eml(String pathname) {
        super(pathname);
        File eml = new File(pathname);
        Properties props = System.getProperties();
        try {

        Session mailSession = Session.getDefaultInstance(props, null);
        InputStream source = new FileInputStream(eml);
         message = new MimeMessage(mailSession, source);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
        e.printStackTrace();
    }


    }



    public MimeMessage getMessage(){
        return message;
    }

        }

