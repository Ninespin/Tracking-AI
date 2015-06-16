/*
 * The MIT License
 *
 * Copyright 2015 Arnaud Paré-Vogt.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package IO.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that allows to load config files and read their content. Config files
 * are in .txt form to allow the user to modify them.
 *
 *
 * <ul>
 * Form :
 * <li>type name value</li>
 * <li>//is a one-line comment that will be deleted on load</li>
 * </ul>
 *
 * @author Arnaud Paré-Vogt
 */
public class Config {

    public static final String COMMENT_STARTER = "//";

    List<Param> content;

    File file;

    /**
     * Creates a new Config pointing at the specified directory. The config file
     * will not be loaded on creation, and if there are no config files, an
     * empty one will be created.
     *
     * @param path the path of the config file. It MUST end with .txt!
     */
    public Config(String path) {
        if(!path.endsWith(".txt"))throw new IllegalArgumentException("The provided path MUST end with '.txt'. The config file must indeed point to a text file.");
        file = new File(path);
        content = new ArrayList<>();
    }

    /**
     * Loads the content of the config file into the config object. After this, it becomes possible to read variables. Warning : all previously stored information will be deleted, you may want to condider writting it before.
     * @throws IOException if an exception ocurs during the reading
     */
    public void load() throws IOException {
        if(!file.exists()){
            return;
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s;
        content.clear();

        boolean mayHaveFailedWarning = false;
        boolean containsDuplicates = false;

        while ((s = br.readLine()) != null) {

            if (s.equals("")) {
                break;//if the line is empty, we do not use it
            }

            if (s.startsWith(COMMENT_STARTER)) {
                
                break;//the line is a comment
            }

            String[] values = s.split(" ");//this should have three values in it : [type,name,value]

            if (values.length != 3) {
                mayHaveFailedWarning = true;
                break;//the lenght is bad. This means the file is probably corrupted.
            }

            if (Type.isPartOf(values[0])==-1) {
                mayHaveFailedWarning = true;
                break;//some types are wrongs
            }

            boolean nameValid = true;//tests if the name is already present
            for (Param content1 : content) {
                nameValid = nameValid && content1.getName().equals(values[1]);
            }
            if (!nameValid) {
                containsDuplicates = true;
                break;//The name is not valid, there are duplicates
            }
            
            Object value;
            Type paramType = Type.values()[Type.isPartOf(values[0])];
            try{
                value = parseValue(values[2],paramType);
            }catch(Exception e){
                break;
            }
            Param parameter = new Param(paramType, values[1], value);
            
            content.add(parameter);
        }
        
        content.sort((Param t, Param t1) -> t.compareTo(t1));
        
        br.close();
    }
    
    /**
     * Attempts to print the config file.
     * @return if the attempt was successfull
     */
    public boolean write(){
        PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter(file,false));
        } catch (IOException ex) {
            return false;
        }
        for (Param content1 : content) {
            pw.println(content1.toString());
        }
        pw.flush();
        pw.close();
        return true;
    }
    
    /**
     * Given a value and a type, attempts to pare it. Not safe.
     * @param value the value
     * @param paramType the type
     * @return the parsed value, of type <code>paramType.getType()</code>
     */
    public Object parseValue(String value,Type paramType){
        switch (paramType){
            case INT:
                return Integer.parseInt(value);
            case STRING:
                return value;
            default:
                return null;
        }
    }
    
    /**
     * Gets a param from the config
     * @param name
     * @param defaultValue
     * @return 
     */
    public String getStringParam(String name, String defaultValue){
        for (Param content1 : content) {
            if(content1.getName().equals(name)){
                if(content1.type == Type.STRING)
                    return (String)content1.getValue();
                else
                    throw new IllegalArgumentException("The given param name does not contain a value of the type 'String'. You may want to consider reviewing the configuration file and/or use a different name.");
            }
        }
        Param p = new Param(Type.STRING,name,defaultValue);
        content.add(p);
        return defaultValue;
    }

    /**
     * Represents a parameter.
     */
    public class Param implements Comparable<Param> {

        private final Type type;
        private Object value;
        private String name;

        public Param(Type type, String name, Object value) {
            this.type = type;
            if (!(type.getType().isAssignableFrom(value.getClass()))) {
                throw new IllegalArgumentException("The given values does not match the given type!");
            }
            this.value = value;
            this.name = name;
        }

        public Type getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return type.getName() + " " + name + " " + value;
        }

        @Override
        public int compareTo(Param p) {
            return this.getName().compareTo(p.getName());
        }
    }

    /**
     * All of the possible types that can be used in the .txt file.
     */
    public enum Type {

        STRING("string", String.class),
        INT("int", Integer.class);
        //COMMENT("comment type",String.class);//makeshift type used to store comments

        public String name;
        public Class type;

        private Type(String name, Class type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Class getType() {
            return type;
        }
        
        /**
         * Looks if the given String matches one on the parameters, and returns its index as if it had been in <code>values()</code>
         * 
         * @param s the name to compare to
         * @return the index of the corresponding element if <code>values()</code>, or -1
         */
        public static int isPartOf(String s) {
            for (int i = 0; i < Type.values().length; i++) {
                if (Type.values()[i].getName().equals(s)) {
                    return i;
                }
            }
            return -1;
        }
    }
}
