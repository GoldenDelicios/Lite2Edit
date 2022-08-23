package goldendelicios.lite2edit;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

public class Lite2Edit {
	private static File dir = new File(System.getProperty("user.dir"));

	public static void main(String[] args) {
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
				String text = "";
				for (File input : inputs)
				{
					try {
						File parent = input.getParentFile();
						dir = parent;
						List<File> outputs = Converter.litematicToWorldEdit(input, parent);
						
						if (outputs.isEmpty()) {
							text += input.getName() + " is not a valid litematic file\n";
						}
						else {
							for (File output : outputs) {
								text += "Exported to " + output.getName() + "\n";
							}
						}
					} catch (Throwable e) {
						text += "Error while converting " + input.getName() + ":\n" + e + "\n";
						e.printStackTrace();
					}
					textArea.setText(text);
				}
			}
		};
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
