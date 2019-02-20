package fr.xgouchet.axml.CompressedXmlParser;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

public class DumpApkPackname {
	public static void main(String[] args) throws IOException {
		String folder = args[0];
		File[] files = new File(folder).listFiles();
		ArrayList<String> names = new ArrayList<>();
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
					// is = new FileInputStream(fileName);
					continue;
				}

				Document doc = new CompressedXmlParser().parseDOM(is);
				Node manifestnode = doc.getChildNodes().item(0);
				NamedNodeMap attrs = manifestnode.getAttributes();
				for (int i = 0, n = attrs.getLength(); i < n; ++i) {
					Node attr = attrs.item(i);
					if (attr.getNodeName().equals("package")) {
						System.out.println("package name " + attr.getNodeValue());
						names.add(attr.getNodeValue());
					}
				}
				// dumpNode(doc.getChildNodes().item(0), "");
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
		PrintWriter out = new PrintWriter("appids.txt");
		for (String name : names) {
			out.println(name);
		}
		out.close();

	}

	private static void dumpNode(Node node, String indent) {
		System.out.println(
				indent + node.getNodeName() + " " + attrsToString(node.getAttributes()) + " -> " + node.getNodeValue());
		NodeList children = node.getChildNodes();
		for (int i = 0, n = children.getLength(); i < n; ++i)
			dumpNode(children.item(i), indent + "   ");
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
}