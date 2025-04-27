package com.ellianw.univates.Entities;

public class Recipe {
    private Integer id = null;
    private String name = null;
    private String description = null;
    private Integer serves = null;
    private Double cost = null;

    public Recipe(Integer id, String name, String description, Integer serves, Double cost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.serves = serves;
        this.cost = cost;
    }

    public Recipe(String name, String description, Integer serves, Double cost) {
        this.name = name;
        this.description = description;
        this.serves = serves;
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getServes() {
        return serves;
    }

    public void setServes(Integer serves) {
        this.serves = serves;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getListName() {
        return id+". "+name;
    }
}
