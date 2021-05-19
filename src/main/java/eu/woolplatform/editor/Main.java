package eu.woolplatform.editor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import eu.woolplatform.editor.functions.WoolProjectFunctions;
import eu.woolplatform.editor.script.model.ActiveWoolProject;
import eu.woolplatform.utils.exception.ParseException;
import eu.woolplatform.wool.model.WoolProject;
import eu.woolplatform.wool.model.WoolProjectMetaData;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {

	public static Version APP_VERSION;

	/**
	 * TODO: Don't read the wool-project.xml location from command line, but from a stored settings file.
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			// Set System L&F
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}

		Main main = new Main();

		if(args.length > 0) {
			String woolProjectMetaDataFileLocation = args[0];
			try {
				WoolProjectMetaData metaData = WoolProjectFunctions.loadLocalWoolProject(new File(woolProjectMetaDataFileLocation));
				main.startMainWindow(metaData);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			main.startLaunchWindow();
		}

		try {
			main.APP_VERSION = Version.readFromResource("version.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Main() {

	}

	private void startLaunchWindow() {
		SwingUtilities.invokeLater(() -> {
			final LaunchWindow window = new LaunchWindow();
			window.setVisible(true);
		});
	}

	private void startMainWindow(WoolProjectMetaData metaData) {
		SwingUtilities.invokeLater(() -> {
			final MainWindow window = new MainWindow(new ActiveWoolProject(metaData));
			window.setVisible(true);
		});
	}
}
