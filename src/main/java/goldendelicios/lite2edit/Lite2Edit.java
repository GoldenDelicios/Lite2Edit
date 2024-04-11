package goldendelicios.lite2edit;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

public class Lite2Edit {
	public static File dir = new File(System.getProperty("user.dir"));
	private static PrintStream errorFile;
	private static ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);

	public static void main(String[] args) {
		try {
			Files.deleteIfExists(Paths.get("errors.log"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (args.length>0) {
			if (args[0].equals("--cli")) {
				new Cli().cli(args);
				return;
			}
		}
		init();
	}

	private static void init()
	{
		JFrame frame = new JFrame("Lite2Edit");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 200);
		frame.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		JLabel label = new JLabel("Pick 1 or more Litematic files");

		JTextArea textArea = new JTextArea(6, 0);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		JButton browse = new JButton("Browse");
		browse.addActionListener(getBrowseListener(textArea, browse));

		panel.add(label);
		panel.add(browse);

		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.getContentPane().add(BorderLayout.SOUTH, scrollPane);
		frame.setVisible(true);
	}

	private static ActionListener getBrowseListener(JTextArea textArea, JButton browse) {
		return event -> {
			JFileChooser fc = new JFileChooser(dir);
			fc.setMultiSelectionEnabled(true);
			fc.setFileFilter(new LitematicFileFilter());
			
			int value = fc.showOpenDialog(browse);
			if (value == JFileChooser.APPROVE_OPTION) {
				File[] inputs = fc.getSelectedFiles();
				browse.setEnabled(false);
				scheduler.execute(() -> {
					StringBuilder s = new StringBuilder();
					for (int i = 0; i < inputs.length; i++) {
						String working = "Working... (" + i + "/" + inputs.length + " complete)";
						SwingUtilities.invokeLater(() -> textArea.setText(working));
						
						long start = System.currentTimeMillis();
						File input = inputs[i];
						try {
							File parent = input.getParentFile();
							dir = parent;
							List<File> outputs = Converter.litematicToWorldEdit(input, parent);
							
							if (outputs.isEmpty()) {
								s.append(input.getName() + " is not a valid litematic file\n");
							}
							else {
								for (File output : outputs) {
									s.append("Exported to " + output.getName() + "\n");
								}
							}
							long time = System.currentTimeMillis() - start;
							System.out.println("Conversion took " + time + "ms");
						} catch (Throwable e) {
							s.append("Error while converting " + input.getName() + ":\n" + e + "\n");
							handleException(e);
						}
					}
					SwingUtilities.invokeLater(() -> {
						textArea.setText(s.toString());
						browse.setEnabled(true);
					});
				});
			}
		};
	}
	
	public static void handleException(Throwable e) {
		e.printStackTrace();
		if (errorFile == null) {
			try {
				errorFile = new PrintStream("errors.log");
			} catch (Exception e2) {
				System.err.println("Failed to write to errors.log");
				e2.printStackTrace();
				return;
			}
		}
		e.printStackTrace(errorFile);
		errorFile.flush();
	}
	
	private static final class LitematicFileFilter extends FileFilter {
		@Override
		public String getDescription() {
			return "Litematics (*.litematic)";
		}
		
		@Override
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().endsWith(".litematic");
		}
	}

}
