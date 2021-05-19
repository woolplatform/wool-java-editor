package eu.woolplatform.editor;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class WoolEditorUITools {

	public static void setBold(JLabel label) {
		label.setFont(label.getFont().deriveFont(label.getFont().getStyle() | Font.BOLD));
	}

	public static void setItalic(JLabel label) {
		label.setFont(label.getFont().deriveFont(label.getFont().getStyle() | Font.ITALIC));
	}

	public static void setTitleFont(JLabel label) {
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, 18));
	}

	public static void setSmallTitleFont(JLabel label) {
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, 14));
	}

	/**
	 * Reads given resource file as a string.
	 *
	 * @param fileName path to the resource file
	 * @return the file's contents
	 * @throws IOException if read fails for any reason
	 */
	public static String getResourceFileAsString(String fileName) throws IOException {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		try (InputStream is = classLoader.getResourceAsStream(fileName)) {
			if (is == null) return null;
			try (InputStreamReader isr = new InputStreamReader(is);
				 BufferedReader reader = new BufferedReader(isr)) {
				return reader.lines().collect(Collectors.joining(System.lineSeparator()));
			}
		}
	}
}
