package fr.xgouchet.axml.CompressedXmlParser;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkPermissionStatsAnalysis {
    public static void main(String[] args) throws IOException {
        String folder = args[0];
        File[] files = new File(folder).listFiles();
        PrintWriter out = new PrintWriter("App.txt");
        ArrayList<String> names = new ArrayList<>();

        for (File file : files) {
            String fileName = file.getAbsolutePath();
            InputStream is = null;
            ZipFile zip = null;
            try {
                if (fileName.endsWith(".apk") || fileName.endsWith(".zip")) {
                    String entryName = args.length > 1 ? args[1] : "AndroidManifest.xml";
                    zip = new ZipFile(fileName);
                    ZipEntry entry = zip.getEntry(entryName);
                    is = zip.getInputStream(entry);
                } else {
                    // is = new FileInputStream(fileName);
                    continue;
                }

                Document doc = new CompressedXmlParser().parseDOM(is);
                Node manifestNode = doc.getChildNodes().item(0);
                NamedNodeMap attrs = manifestNode.getAttributes();

                for (int i = 0, n = attrs.getLength(); i < n; ++i) {
                    Node attr = attrs.item(i);
                    if (attr.getNodeName().equals("package")) {
                        System.out.println("package name " + attr.getNodeValue());
                        names.add(attr.getNodeValue());
                        out.println(attr.getNodeValue());
                    }
                }

                dumpNode(doc.getChildNodes().item(0), "",attrs,0);
            } catch (Exception e) {
                System.err.println("Failed AXML decode: " + e);
                e.printStackTrace();
            }
            if (is != null) {
                is.close();
            }
            if (zip != null) {
                zip.close();
            }
        }
        out.close();

    }

    private static void dumpNode(Node node, String indent, NamedNodeMap attrs, int level) {
/*        if(level == 0){
            for (int i = 0, n = attrs.getLength(); i < n; ++i) {
                Node attr = attrs.item(i);
                if (attr.getNodeName().equals("package") & count < 1) {
                        System.out.println("package name 2: " + attr.getNodeValue());
                        count ++;
                    try
                    {
                        String filename= "PermissionList.txt";
                        FileWriter fw = new FileWriter(filename,true); //the true will append the new data
                        fw.write(
                                //appends the string to the file
                                "package name -- : " + attr.getNodeValue()+"\n");
                        fw.close();
                    }
                    catch(IOException ioe)
                    {
                        System.err.println("IOException: " + ioe.getMessage());
                    }
                }
            }
        }*/


        if (node.getNodeName().equals("uses-permission")) {
            for (int i = 0, n = attrs.getLength(); i < n; ++i) {
                Node attr = attrs.item(i);
                if (attr.getNodeName().equals("package")) {
                    //System.out.print("package name 3: " + attr.getNodeValue() + " - ");
                }
            }
            //System.out.println(indent + node.getNodeName() + " " + attrsToString(node.getAttributes()) + " -> " + node.getNodeValue() + " -> " + level);

            try
            {
                String filename= "PermissionList.csv";
                FileWriter fw = new FileWriter(filename,true); //the true will append the new data

                for (int i = 0, n = attrs.getLength(); i < n; ++i) {
                    Node attr = attrs.item(i);
                    if (attr.getNodeName().equals("package")) {
                        fw.write("Package Name : " + attr.getNodeValue() + ",");
                    }
                }

                fw.write(indent + node.getNodeName() + "," + attrsToString(node.getAttributes()) + " ," + node.getNodeValue()+ "," + "\n");
                fw.close();
            }
            catch(IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }
        }

        NodeList children = node.getChildNodes();
        int count = 0;
        for (int i = 0, n = children.getLength(); i < n; ++i) {
            if (children.item(i).getNodeName().equals("uses-permission")) {
                count++;
            }
            //System.out.println(children.item(i).getNodeName());
            dumpNode(children.item(i), indent + "   ", attrs, level++);
        }

        if(count > 0){
            String name = null;
            for (int i = 0, n = attrs.getLength(); i < n; ++i) {
                Node attr = attrs.item(i);
                if (attr.getNodeName().equals("package")) {
                    name = attr.getNodeValue();
                }
            }
            try
            {
                String filename= "PermissionList.csv";
                FileWriter fw = new FileWriter(filename,true); //the true will append the new data
                fw.write("Package Name : " + name + " ,uses-permission, , Total:," + count + "\n");
                fw.close();
            }
            catch(IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }
           // System.out.println("Permision total: " + count);
        }
    }

    private static String attrsToString(NamedNodeMap attrs) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0, n = attrs.getLength(); i < n; ++i) {
            if (i != 0)
                sb.append(", ");
            Node attr = attrs.item(i);
            sb.append(attr.getNodeName() + "=" + attr.getNodeValue());
        }
        sb.append(']');
        return sb.toString();
    }

    private void writeToCSV(String input){
        try
        {
            String filename= "PermissionList.csv";
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data

            fw.write(input);
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
}
