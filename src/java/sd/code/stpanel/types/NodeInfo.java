/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd.code.stpanel.types;

import java.util.ArrayList;

/**
 *
 * @author motaz
 */
public class NodeInfo {
    
    private String nodeName;
    ArrayList<String> data;
    boolean isnumeric;
    
    public String getNodeName(){ return nodeName; }
    
    public NodeInfo(String node){
        nodeName = node.replaceAll("\\[", " ");
        nodeName = nodeName.replaceAll("\\]", " ").trim();
        isnumeric = ! isNumeric(nodeName);
        data = new ArrayList<>();
    }
    
    public boolean isTrunk(){
	String istrunk = getProperty("trunk");
	if (istrunk == null){
	    istrunk = "";
	}
        return (isnumeric || istrunk.toLowerCase().trim().equals("yes"));
    }
    public boolean isExtension(){
        return ! isTrunk();
    }
    public void addLine(String line){
        
        data.add(line);
        
    }
    
    private boolean isNumeric(String str)
    {
        if ((str != null) && (! str.equals(""))){
          return str.matches("[+-]?\\d*(\\.\\d+)?");
        }
        else{
          return(false);
        }
    }  
    
    public String getProperty(String name){
        
        name = name.toLowerCase();
        for (int i=0; i < data.size(); i++){
            String dataLine = data.get(i).trim();
            if (dataLine.toLowerCase().indexOf(name) == 0) {
               String value = dataLine.substring(dataLine.indexOf("=") + 1, dataLine.length());
               return value;
            }
        }
        return "";
    }
    
}
