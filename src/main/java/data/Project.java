package data;

import discord4j.common.util.Snowflake;

public class Project {
    private String name;
    private long author;
    private String description;
    private Snowflake roleId;

    public Project(String name, long author, String description, Snowflake roleId) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.roleId = roleId;
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

    public Snowflake getRoleId() {
        return roleId;
    }
}
