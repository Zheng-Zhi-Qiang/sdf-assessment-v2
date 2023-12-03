package email;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length <= 1){
            System.err.println("Please provide both data file and template file!");
            return;
        }
        else if (args.length > 2){
            System.err.println("Too many arguments provided!");
            return;
        }

        String dataFile = args[0];
        String templateFile = args[1];
        Template template = new Template(templateFile);

        FileReader fr = new FileReader(dataFile);
        BufferedReader br = new BufferedReader(fr);
        
        String[] colNames = br.readLine().trim().split(",");
        br.lines().forEach(line ->
            System.out.println(template.fillTemplate(colNames, line.trim().split(","))));

        br.close();
        fr.close();
    }
}
