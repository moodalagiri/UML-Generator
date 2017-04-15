package umlgen;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.rmi.ssl.SslRMIClientSocketFactory;

public class Umlgen {

    public static String s = "@startuml\n skinparam classAttributeIconSize 0 \n";
    public static String classname;
    public static int mc =0;
    public static ArrayList<String> classes = new ArrayList<String>();
    public static ArrayList<String> duplicate = new ArrayList<String>();

    public static ArrayList<String> dependency = new ArrayList<String>();
    public static ArrayList<String> relation = new ArrayList<String>();
    public static ArrayList<String> variables = new ArrayList<String>();
     public static void main(String[] args) throws Exception {
            // creates an input stream for the file to be parsed
       //  System.out.println("Arguments are: "+args[2]+" "+args[3]);
          File dir = new File(args[0]);
          File[] directoryListing = dir.listFiles();
          if (directoryListing != null) {
        	  
        	  /* array of class names */
        	  
        	  for (File child : directoryListing) {
        		    
                  //    System.out.println(child);
        		  ArrayList<String> tokens = new ArrayList<String>();
                      if((child.getName().contains(".java")))
                      {
                        
                        File file = new File(child.getPath());

                        FileInputStream in = new FileInputStream(file);
                        CompilationUnit cu;
                        try {
                         
                          cu = JavaParser.parse(in,"UTF8");
                        }
                        
                      finally {
                          in.close();
                      }
                      String temp = cu.toString();
                      
                      ArrayList<String> line = new ArrayList<String>();
                      
                      String lines[] = temp.split("\\r?\\n");
                      
                      for(int i=0;i<lines.length;i++)
                      {
                    	  line.add(lines[i]);
                    	  for(String l:line)
                    	  {
                    		  String delims = "[ .,?!]+";
                    		  String line_token[]=l.split(delims);
                    		  for(int j=0;j<line_token.length;j++)
                    		  {
                    			  tokens.add(line_token[j]);
                    		  }
                          }
                      }
                      //int i=0;
                      for(int k=0;k<tokens.size();k++)
                      {
                    	  if(tokens.get(k).contains("class"))
                    	  {
                    		  classname=tokens.get(k+1);
                    		  classes.add(classname);
                    		//  dependency.add(classname);
                    	  }
                    	  if(tokens.get(k).contains("interface"))
                    	  {
                    		  classname=tokens.get(k+1);
                    		  dependency.add(classname);
                    		  classes.add(classname);
                    	  }
                          
                      }
                     
                  }
        	  }
        	  //System.out.println("Classes arraylist: "+classes);
        	  //System.out.println("Dependency arraylist: "+dependency);
        	  
          
        	  /* array of class names end */
        	  
            for (File child : directoryListing) {
          
                if((child.getName().contains(".java")))
                {
                    
                    
                    
                File file = new File(child.getPath());
          
                FileInputStream in = new FileInputStream(file);
                CompilationUnit cu;
                try {
                   
                    cu = JavaParser.parse(in,"UTF8");
                    }
                finally {
                    in.close();
                }
                String temp = cu.toString();
                String lines[] = temp.split("\\r?\\n");
                String delims = "[ .,?!]+";
                String[] tokens = lines[0].split(delims);

                List types = cu.getTypes(); 

                List types1 = cu.getTypes();

                 
                
                TypeDeclaration typeDec = (TypeDeclaration) types.get(0);
          
                classname = typeDec.getName();
                
                if(tokens[1].equals("interface"))
                    s = s + "interface" + " " + classname + "\n";
                if(tokens[1].equals("class"))
                     s = s + "class" + " " + classname + "\n";
                // visit and print the methods names
                
                new MethodVisitor6().visit(cu, null);
                new MethodVisitor().visit(cu, null);
                new MethodVisitor7().visit(cu, null);
                new MethodVisitor1().visit(cu, null);
                new MethodVisitor5().visit(cu, null);
                new MethodVisitor2().visit(cu, null);
                new MethodVisitor3().visit(cu, null);
                new MethodVisitor4().visit(cu, null);
            }
            }
            s = s + "@enduml\n";
           System.out.println(s); 
           String destination=args[1];
            generateUML p = new generateUML();
            p.umlCreator(s,destination);
        
          }
          
     }
     private static class MethodVisitor extends VoidVisitorAdapter {

            @Override
            public void visit(FieldDeclaration n, Object arg) {
               
               int flag=0;
               //System.out.println("fields: \n"+n.toString());
                String k =n.toString();
                 k = k.replaceAll("[;]", "");
                 String[] strs = k.split("\\s+");
                 if(strs[0].equals("public"))
                 {
                     strs[0]="+" ;
                     flag=1;
                 }
                 if(strs[0].equals("private"))
                 {
                	 strs[0]="-" ;
                	   flag=1;
                 
                 }
                
                 if(flag>0){
                	 //System.out.println("strs[2]: "+strs[2]+" strs[0]: "+strs[0]);
                	 if((variables.contains(strs[2])) && (strs[0].equals("-")))
                     {
                		 
                		
                     	strs[0]="+";
                     	//System.out.println("modifier: "+strs[0]);
                     }
                 s = s + classname + " : " + strs[0] + " " + strs[1] + " " + strs[2];
                 s = s + "\n";
                 }
           
                 super.visit(n, arg);
            }
            
            
}
     
     private static class MethodVisitor6 extends VoidVisitorAdapter {
    	 public void visit(MethodDeclaration n, Object arg)
    	 {
   		  
             if(n.getName().contains("get") && (n.getModifiers()==1))
                 
             {
                 
                 String method_name=n.getName().toLowerCase();
                 String var=method_name.substring(3,method_name.length());
                 variables.add(var);
                 
             }
             
             else if(n.getName().contains("set") && (n.getModifiers()==1))
             {
                 String method_name=n.getName().toLowerCase();
                 String var=method_name.substring(3,method_name.length());
                 variables.add(var);
                 }
    	 }
     }
     
     private static class MethodVisitor7 extends VoidVisitorAdapter {

         
         @Override
         public void visit(ConstructorDeclaration n, Object arg) {
             
        	 String mod = null;
         		if(n.getModifiers()==1)
         		{
         			mod="+";
         		}
         		else if(n.getModifiers()==2)
         		{
         			mod="-";
         		}
         		else if(n.getModifiers()==4)
         		{
         			mod="#";
         		}
         		else
         		{
         			mod="+";
         		}
         		//System.out.println("function "+ n.getName() +" has " +n.getModifiers());
                s = s + classname + " : "+ mod + n.getName() + "()" ;
                  
                s = s + "\n";
 
         }
     }
     
     
     
     
     
     
     
     private static class MethodVisitor1 extends VoidVisitorAdapter {

          
            @Override
            public void visit(MethodDeclaration n, Object arg) {
                

                if(!((n.getName().contains("get")) || (n.getName().contains("set")))) 
                {
                	String mod = null;
                	if(n.getModifiers()==1)
                	{
                		mod="+";
                	}
                	else if(n.getModifiers()==2)
                	{
                		mod="-";
                	}
                	else if(n.getModifiers()==4)
                	{
                		mod="#";
                	}
                	else
             		{
             			mod="+";
             		}
                     s = s + classname + " : "+mod + n.getName() + "():"+ n.getType() ;
                     
                     s = s + "\n";
                }
                
                
                //System.out.println("The body part of method: "+n.getName()+"\n");
                //System.out.println(n.getBody());
                
                /*New code for uses START*/
                ArrayList<String> tokens = new ArrayList<String>();
                if(n.getBody()!=null)
                {
                String temp = n.getBody().toString();
                ArrayList<String> line = new ArrayList<String>();
                
                String lines[] = temp.split("\\r?\\n");
                
                for(int i=0;i<lines.length;i++)
                {
              	  line.add(lines[i]);
                }
              	for(String l:line)
              	{
              		  String delims = "[ .,?!]+";
              		  String line_token[]=l.split(delims);
              		  for(int j=0;j<line_token.length;j++)
              		  {
              			  tokens.add(line_token[j]);
              		  }
                }
              	for(int k=0;k<tokens.size();k++)
              	{
              		if(dependency.contains(tokens.get(k)))
              		{
              			s = s + classname + " ..> " + tokens.get(k) + "\n";
              		}
              	}
                
                }
                /* New Code for uses END */
                
                String[] coll;
             
                if(n.getParameters()!=null)
                {
                	//System.out.println("Parameters for class: \n"+classname+" "+n.getParameters().toString());
   			 		coll= n.getParameters().toString().split("[\\[\\s]");
   			 	if (!(duplicate.contains(coll[1])))
   			 	{
   			 	duplicate.add(coll[1]);
   			 	if((dependency).contains(coll[1]))
   			 	{
     			
     				  s=s + classname + " ..> " + coll[1] + "\n";
     			  }
   			 	}
   			 		
                
                }
            }
                
            }
            
            

     private static class MethodVisitor2 extends VoidVisitorAdapter {

          
         @Override
         public void visit(ClassOrInterfaceDeclaration decl, Object arg)
           {
              // Make class extend//
             
             List<ClassOrInterfaceType> list = decl.getExtends();
             if(list==null)
                 return;
             for (ClassOrInterfaceType k : list) {
                    String n = k.toString();
                    s = s + n + " " + "<|--" + " " + classname + "\n";
                    
                }
              
              
           }
            
}

     private static class MethodVisitor3 extends VoidVisitorAdapter {

          
         @Override
         public void visit(ClassOrInterfaceDeclaration decl, Object arg)
           {
              // Make class extend Blah.
             
             
             List<ClassOrInterfaceType> list = decl.getImplements();
             if(list==null)
                 return;
             for (ClassOrInterfaceType k : list) {
                    String n = k.toString();
                    s = s + n + " " + "<|.." + " " + classname + "\n";
                   
                }
            
              
           }    
           }
     
     private static class MethodVisitor4 extends VoidVisitorAdapter {


         @Override
         public void visit(FieldDeclaration n, Object arg)
           {
        	 
        	 //System.out.println("relation arraylist" + relation);
        	 
        	 if((classes).contains(n.getType().toString()))
    		 {
    			 String rel=classname+n.getType().toString();
    			 /*
    			  * Code to reverse string START
    			  */
    			 String input=rel;
    			  StringBuilder input1 = new StringBuilder();
    			  input1.append(input);
    			  input1=input1.reverse(); 
    			 
    			 /*
    			  * Code to reverse string END 
    			  */

    			  //System.out.println("relation : "+relation);
    			  if(((relation).contains(input1.toString())))
    			  {
    				 
    			  }
    			  else
    			  {
    				  relation.add(rel);
    				  s=s + classname + "\"1\" -- \"1\"" + n.getType().toString() + "\n";
    			  }
    		 }
    		 else if((n.getType().toString().contains("Collection")))
    		 {
    			 String[] coll;
    			 coll= n.getType().toString().split("[<>]");
    			 String rel=classname+coll[1];
    			 /*
    			  * Code to reverse string START
    			  */
    			  String input=rel;
    			  StringBuilder input1 = new StringBuilder();
    			  input1.append(input);
    			  input1=input1.reverse(); 

    			  if((classes).contains(coll[1]))
    			 {
        			// System.out.println("relation : "+relation);
        			 if(((relation).contains(input1.toString())))
        			 {
        			 }
        			 else
        			 {
        				 relation.add(rel);
        				 s=s+ classname +"\"1\" -- \"*\"" + coll[1] + "\n";
        			 }
    			 }
    		 }
        	}
             }
     /* constructor declaration */
     private static class MethodVisitor5 extends VoidVisitorAdapter<Object> {

         @Override
         public void visit(ConstructorDeclaration n, Object arg) {
             String[] coll;
        	 if(n.getParameters()!=null)
             {
             	//System.out.println("Parameters for class: \n"+classname+" "+n.getParameters().toString());
			 		coll= n.getParameters().toString().split("[\\[\\s]");
			 	
			 		//System.out.println("coll[1]: "+coll[1]);
			    
             if((dependency).contains(coll[1]))
            {
    			
    				  s=s + classname + " ..> " + coll[1] + "\n";
    			  }
             }
       }
           
     }
}