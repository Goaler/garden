package org.garden.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 扫描指定包下的类返回类名
 * 
 * @author CorgiKing
 *
 */
public class PackageScanner {
	private static final Logger log = LoggerFactory.getLogger(PackageScanner.class);

	private PackageScanner() {
	}

	/**
	 * 从资源文件和依赖Jar中获取类名
	 * 
	 * @param basePackage
	 * @return
	 */
	public static List<String> scanSourceFolderAndDependencies(String basePackage) {
		Enumeration<URL> resource = getResources(basePackage);
		if (resource == null) {
			return null;
		}
		List<String> classNames = new ArrayList<>();
		while (resource.hasMoreElements()) {
			URL url = resource.nextElement();
			scanSourceFolder(basePackage, classNames, url);
			scanJarFile(basePackage, classNames, url);
		}
		return classNames;
	}

	/**
	 * 从依赖Jar中查找类名
	 * 
	 * @param basePackage
	 * @return
	 * @throws IOException
	 */
	public static List<String> scanDependencies(String basePackage) {
		Enumeration<URL> resource = getResources(basePackage);
		if (resource == null) {
			return null;
		}
		List<String> classNames = new ArrayList<>();
		while (resource.hasMoreElements()) {
			URL url = resource.nextElement();
			scanJarFile(basePackage, classNames, url);
		}
		return classNames;
	}

	/**
	 * 从资源文件中获取类名
	 * 
	 * @param basePackage
	 * @return
	 */
	public static List<String> scanSourceFolder(String basePackage) {
		URL url = PackageScanner.class.getResource("/" + basePackage.replace('.', '/'));
		List<String> classNames = new ArrayList<>();
		if (url != null && "file".equalsIgnoreCase(url.getProtocol())) {
			String scanPath = url.getPath();
			log.trace("扫描 :{}", scanPath);
			scanDir(new File(scanPath), basePackage, classNames);
			return classNames;
		}
		return classNames;
	}

	/**
	 * 从目录中查找类名,可从外部文件中扫描类名
	 * 
	 * @param dir
	 * @param basePackage
	 * @return
	 */
	public static List<String> scanDir(File dir, String basePackage) {
		List<String> classNames = new ArrayList<>();
		scanDir(dir, basePackage, classNames);
		return classNames;
	}

	/**
	 * 获取class文件类名
	 * 
	 * @param file
	 * @param basePackage
	 * @return
	 */
	private static String scanFile(File classFile, String basePackage) {
		if (classFile.getName().endsWith(".class")) {
			String name = classFile.getPath().replace(File.separatorChar, '.');
			int s = name.lastIndexOf(basePackage);
			int e = name.lastIndexOf('.');
			name = name.substring(s, e);
			return name;
		}
		return null;
	}

	/**
	 * 从目录中查找类名
	 * 
	 * @param file
	 * @param basePackage
	 * @param classNames
	 */
	private static void scanDir(File dir, String basePackage, List<String> classNames) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File f : files) {
				scanDir(f, basePackage, classNames);
			}
		} else {
			String className = scanFile(dir, basePackage);
			if (className != null) {
				classNames.add(className);
			}
		}
	}
	
	private static void scanJarFile(String basePackage, List<String> classNames, URL url) {
		String protocol = url.getProtocol();
		if ("jar".equalsIgnoreCase(protocol)) {
			try {
				JarURLConnection con = (JarURLConnection) url.openConnection();
				if (con != null) {
					JarFile jarFile = con.getJarFile();
					if (jarFile != null) {
						log.trace("扫描{}", jarFile.getName());
						Enumeration<JarEntry> entries = jarFile.entries();
						while (entries.hasMoreElements()) {
							JarEntry jarEntry = entries.nextElement();
							// org/corgiking/CKLogger.class
							String name = jarEntry.getName();
							if (name.endsWith(".class") && name.startsWith(basePackage.replace('.', '/'))) {
								classNames.add(name.substring(0, name.lastIndexOf('.')).replace('/', '.'));
							}
						}
					}
				}
			} catch (IOException e) {
				log.error("扫描Jar文件出错!Jar-{},Error-{}", url, e.getMessage());
			}
		}
	}
	
	private static void scanSourceFolder(String basePackage, List<String> classNames, URL url) {
		String protocol = url.getProtocol();
		if ("file".equalsIgnoreCase(protocol)) {
			String scanPath = url.getPath();
			log.trace("扫描 :{}", scanPath);
			scanDir(new File(scanPath), basePackage, classNames);
		}
	}
	
	private static Enumeration<URL> getResources(String basePackage) {
		try {
			return PackageScanner.class.getClassLoader().getResources(basePackage.replace(".", "/"));
		} catch (IOException e) {
			log.error("Get resources error. basePackage:{}. Error message:{}", basePackage, e.getMessage());
			return null;
		}
	}

	public static void main(String[] args) {
		List<String> classNames = PackageScanner.scanSourceFolder("org");
		System.out.println("类数量:" + classNames.size());
		 for(String name:classNames){
		 System.out.println(name);
		 }
	}
}
