package email;

import java.io.BufferedReader;
import java.io.FileReader;

public class Template {

    private String template;
    
    public Template(String template_file) throws Exception {
        this.template = readTemplateFile(template_file);
    }

    private String readTemplateFile(String template_file) throws Exception {
        FileReader fr = new FileReader(template_file);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder();

        br.lines().forEach(line -> {
            line = line.trim();
            if (line.length() <= 0){
                sb.append("\n");
            }
            else {
                sb.append(line);
                sb.append("\n");
            }
        });

        br.close();
        fr.close();
        return sb.toString();
    }

    public String fillTemplate(String[] cols, String[] data){
        String filledTemplate = template;
        for (int i = 0; i < cols.length; i++){
            filledTemplate = filledTemplate.replace("__%s__".formatted(cols[i]), data[i]);
        }

        filledTemplate = filledTemplate.replace("\\n", "\n");
        return filledTemplate;
    }
}