package data;

public class Project {
    private String name;
    private long author;
    private String description;

    public Project(String name, long author, String description) {
        this.name = name;
        this.author = author;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public long getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }
}
