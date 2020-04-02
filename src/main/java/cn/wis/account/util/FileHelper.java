package cn.wis.account.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileAlreadyExistsException;

import cn.hutool.core.util.StrUtil;

public final class FileHelper {

	public static long getUsableSpace(String path) {
		if (StrUtil.isEmpty(path)) {
			throw new IllegalArgumentException("The path is empty");
		}
		File file = new File(path);
		if (file.isDirectory()) {
			return file.getUsableSpace();
		}
		throw new RuntimeException("The path isn't a directory: " + path);
	}

	public static boolean createDirectoryIfNotExist(String path)
			throws FileAlreadyExistsException {
		if (StrUtil.isEmpty(path)) {
			throw new IllegalArgumentException("The path is empty");
		}
		File file = new File(path);
		if (file.isFile()) {
			throw new FileAlreadyExistsException("The path is a file: " + path);
		}
		return file.isDirectory() || file.mkdirs();
	}

	public static void save(Object object, String pathname, boolean cover)
			throws IllegalAccessException, IOException {
		if (object == null || pathname == null) {
			throw new NullPointerException("The object or pathname is null");
		}
		File file = new File(pathname);
		if (file.isDirectory()) {
			throw new IllegalAccessException("The pathname is a directory: " + pathname);
		}
		if (!cover && file.exists()) {
			throw new FileAlreadyExistsException("The file has already exists: " + pathname);
		}
		if (!file.exists() && !file.createNewFile()) {
			throw new IllegalAccessException("Cannot create the file: " + pathname);
		}
		try (FileOutputStream stream = new FileOutputStream(file);
				ObjectOutputStream output = new ObjectOutputStream(stream);) {
			output.writeObject(object);
			output.flush();
		} catch (Exception e) {
			throw new IOException("Fail to write object to file: " + pathname, e);
		}
	}

	public static Object read(String pathname)
			throws IllegalAccessException, IOException {
		if (StrUtil.isEmpty(pathname)) {
			throw new IllegalArgumentException("The pathname is empty");
		}
		File file = new File(pathname);
		if (!file.exists()) {
			throw new FileNotFoundException("The file doesn't exist: " + pathname);
		}
		if (file.isDirectory()) {
			throw new IllegalAccessException("The file is a directory: " + pathname);
		}
		try (FileInputStream stream = new FileInputStream(file);
				ObjectInputStream input = new ObjectInputStream(stream);) {
			return input.readObject();
		} catch (Exception e) {
			throw new IOException("Fail to read object from file: " + pathname, e);
		}
	}

	private FileHelper() {
		throw new RuntimeException("There isn't instance of file helper for you");
	}

}
