package data;

import discord4j.common.util.Snowflake;

public class Project {
    private final String name;
    private final Snowflake author;
    private final String description;
    private final Snowflake roleId;
    private final Snowflake categoryId;

    public Project(String name, Snowflake author, String description, Snowflake roleId, Snowflake categoryId) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.roleId = roleId;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public Snowflake getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public Snowflake getRoleId() {
        return roleId;
    }

    public Snowflake getCategoryId() {
        return categoryId;
    }
}
