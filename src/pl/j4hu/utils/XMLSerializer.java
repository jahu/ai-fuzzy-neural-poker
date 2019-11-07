package pl.j4hu.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class XMLSerializer {
	public static void write(Object obj, String filename) {
		try {
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));
			encoder.writeObject(obj);
			encoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	public static Object read(String filename) {
		Object obj = null;
		try {
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
			obj = decoder.readObject();
			decoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
