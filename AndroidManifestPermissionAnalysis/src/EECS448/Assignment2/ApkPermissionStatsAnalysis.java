/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package EECS448.Assignment2;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkPermissionStatsAnalysis {
	public static void main(String[] args) throws IOException {
        //passing the apk Folder path as 1st argument 2nd argument as the full xml name;
		String folder = args[0];
		File[] files = new File(folder).listFiles();
		HashMap<String, Integer> permissionCount = new HashMap<>();
		HashMap<String, Integer> appPermissionCount = new HashMap<>();

		for (File f : files) {
			String fileName = f.getAbsolutePath();
			InputStream is = null;
			ZipFile zip = null;
			try {
				if (fileName.endsWith(".apk") || fileName.endsWith(".zip")) {
					String entryName = args.length > 1 ? args[1] : "AndroidManifest.xml";
					zip = new ZipFile(fileName);
					ZipEntry entry = zip.getEntry(entryName);
					is = zip.getInputStream(entry);
				} else {
					continue;
				}

				Document doc = new CompressedXmlParser().parseDOM(is);
				Node manifestnode = doc.getChildNodes().item(0);
				NamedNodeMap attrs = manifestnode.getAttributes();
				String packageName = null;
				for (int i = 0, n = attrs.getLength(); i < n; ++i) {
					Node attr = attrs.item(i);
					if (attr.getNodeName().equals("package")) {
						packageName = attr.getNodeValue();
						//System.out.println("package name " + attr.getNodeValue());
					}
				}
				appPermissionCount.put(packageName,calculate(doc.getChildNodes().item(0),permissionCount).size());

			} catch (Exception e) {
				System.err.println("Failed XML decode: " + e);
				e.printStackTrace();
			}
			if (is != null) {
				is.close();
			}
			if (zip != null) {
				zip.close();
			}
		}
		System.out.println("-----------------Start output TotalPermissionCount----------------------");

		permissionCount = sortHashMapByValues(permissionCount);
		writeToCSV("TotalPermissionCount","Permission", permissionCount);

		System.out.println("-----------------Start output AppPermissionCount----------------------");

		appPermissionCount = sortHashMapByValues(appPermissionCount);
		writeToCSV("AppPermissionCount","App-Name", appPermissionCount);

		System.out.println("-----------------Finished----------------------");
	}

	private static List<String> calculate(Node node,HashMap<String, Integer> permissionCount) {

		ArrayList<String> permissions = new ArrayList<>();
		NodeList children = node.getChildNodes();

		for (int i = 0, n = children.getLength(); i < n; ++i) {
			if (children.item(i).getNodeName().equals("uses-permission")) {
				String permission = attrsToString(children.item(i).getAttributes());
				try
				{
					String filename= "Permission.txt";
					FileWriter fw = new FileWriter(filename,true);
					fw.write(children.item(i).getNodeName() + " " + permission + " -> " + children.item(i).getNodeValue()+"\n");
					fw.close();
				}
				catch(IOException ioe)
				{
					System.err.println("IOException: " + ioe.getMessage());
				}
				//System.out.println(children.item(i).getNodeName() + " " + attrsToString(children.item(i).getAttributes()) + " -> " + children.item(i).getNodeValue());

				if(permissionCount.get(permission) != null ){
					permissionCount.put(permission,permissionCount.get(permission)+1);
				}else{
					permissionCount.put(permission,1);
				}
				permissions.add(permission);
			}
		}

		//System.out.println("Total:" + permissions.size());
		return permissions;

	}

	private static String attrsToString(NamedNodeMap attrs) {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node attr = attrs.item(0);
		sb.append(attr.getNodeName() + "=" + attr.getNodeValue());
		sb.append(']');
		return sb.toString();
	}

	public static LinkedHashMap<String, Integer> sortHashMapByValues(HashMap<String, Integer> passedMap) {
		List<String> mapKeys = new ArrayList<>(passedMap.keySet());
		List<Integer> mapValues = new ArrayList<>(passedMap.values());

		Collections.sort(mapValues,Collections.reverseOrder());
		Collections.sort(mapKeys);

		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

		Iterator<Integer> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Integer val = valueIt.next();
			Iterator<String> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				String key = keyIt.next();
				Integer comp1 = passedMap.get(key);
				Integer comp2 = val;

				if (comp1.equals(comp2)) {
					keyIt.remove();
					sortedMap.put(key, val);
					break;
				}
			}
		}
		return sortedMap;
	}

	private static void writeToCSV(String fileName,String keyName, HashMap<String, Integer> inputStream){
		try
		{
			String filename= fileName + ".csv";
			FileWriter fw = new FileWriter(filename,true); //the true will append the new data
			for(String s : inputStream.keySet()){
				fw.write(keyName +":" + s + ", Count:," + inputStream.get(s) + " \n");
				//System.out.println(keyName + ":" + s + ", Count:," + inputStream.get(s));
			}

			fw.close();
		}
		catch(IOException ioe)
		{
			System.err.println("IOException: " + ioe.getMessage());
		}
	}
}