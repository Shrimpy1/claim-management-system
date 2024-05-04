package rmit.furtherprog.claimmanagementsystem.data.model.prop;

public class Document {
    private String fileName;

    public Document() {
    }

    public Document(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
