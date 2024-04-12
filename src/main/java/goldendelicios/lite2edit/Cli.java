package goldendelicios.lite2edit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Cli {

    public void cli(String[] args) {
        ArrayList<File> files = new ArrayList<>();
        System.out.println("----Starting Lite2Edit CLI----");
        if (args.length>1) {

            //Get all files from arguments
            for (String fn: args) {
                if (!fn.equals("--cli")) {
                    if (fn.contains(".litematic")) {
                        File file = new File(fn);
                        if (file.exists()) {
                            files.add(file);
                        }
                        else{
                            System.out.println(fn+" doesn't exist!");
                        }
                    }
                    else {
                        System.out.println(fn+" is not a litematic file!");
                    }
                }
            }

            //conversion logic
            for (int i = 0; i<files.size(); i++) {
                System.out.println("Working... (" + i + "/" + files.size() + " complete)");

                long start = System.currentTimeMillis();
                File input = files.get(i);
                try {
                    File parent = input.getParentFile();
                    Lite2Edit.dir = parent;
                    List<File> outputs = Converter.litematicToWorldEdit(input, parent);

                    if (outputs.isEmpty()) {
                        System.out.println(input.getName() + " is not a valid litematic file\n");
                    }
                    else {
                        for (File output : outputs) {
                            System.out.println("Exported to " + output.getName() + "\n");
                        }
                    }
                    long time = System.currentTimeMillis() - start;
                    System.out.println("Conversion took " + time + "ms");
                } catch (Throwable e) {
                    System.out.println("Error while converting " + input.getName() + ":\n" + e + "\n");
                    Lite2Edit.handleException(e);
                }
            }
        }
        else {
            System.out.println("No valid file paths found!");
            System.out.println("Correct usage: java -jar Lite2Edit-x.x.x.jar --cli [Path to file 1] [Path to file 2]...");
        }
    }
}
