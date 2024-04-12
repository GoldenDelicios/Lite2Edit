package goldendelicios.lite2edit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Cli {

    private ArrayList<File> files = new ArrayList<>();
    //all possible arguments

    public void cli(String[] args) {
        System.out.println("----Starting Lite2Edit CLI----");
        if (args.length>1) {
            for (String fn: args) {
                if (!fn.equals("--cli")) {
                    File file = new File(fn);
                    if (file.exists()) {
                        files.add(file);
                        System.out.println(fn+"ssss");
                    }
                    System.out.println(fn);
                }
            }

            StringBuilder s = new StringBuilder();


            for (int i = 0; i<files.size(); i++) {
                String working = "Working... (" + i + "/" + files.size() + " complete)";

                long start = System.currentTimeMillis();
                File input = files.get(i);
                try {
                    File parent = input.getParentFile();
                    Lite2Edit.dir = parent;
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
                    Lite2Edit.handleException(e);
                }
            }
        }
        else {
            System.out.println("No valid filepaths found!");
            System.out.println("Correct usage: java -jar Lite2Edit-x.x.x.jar --cli [Path to file 1] [Path to file 2]...");
        }
    }
}
