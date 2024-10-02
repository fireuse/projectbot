package data;

public class Project {
    private String name;
    private long author;

    public Project(String name, long author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public long getAuthor() {
        return author;
    }
}
