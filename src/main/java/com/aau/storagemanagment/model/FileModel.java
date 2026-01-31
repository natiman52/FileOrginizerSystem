package com.aau.storagemanagment.model;

import java.util.Map;
import java.util.HashMap;

public class FileModel extends AbstractModel {
    private String filepath;
    private String name;
    private long size;
    private String createdDate;
    private String category;
    private int ownerId;

    public FileModel(String filepath, String name, long size, String createdDate, String category, int ownerId) {
        this.filepath = filepath;
        this.name = name;
        this.size = size;
        this.createdDate = createdDate;
        this.category = category;
        this.ownerId = ownerId;
    }

    public String getFilepath() { return filepath; }
    public String getName() { return name; }
    public long getSize() { return size; }
    public String getCreatedDate() { return createdDate; }
    public String getCategory() { return category; }
    public int getOwnerId() { return ownerId; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public Object getIdentifier() {
        return filepath;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("filepath", filepath);
        m.put("name", name);
        m.put("size", size);
        m.put("createdDate", createdDate);
        m.put("category", category);
        m.put("ownerId", ownerId);
        return m;
    }
}
