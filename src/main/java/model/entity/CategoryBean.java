package model.entity;

import java.io.Serializable;

public class CategoryBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;

    // デフォルトコンストラクタ
    public CategoryBean() {
    }

    // 引数付きコンストラクタ
    public CategoryBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryBean{id=" + id + ", name='" + name + "'}";
    }
    
//test
    public static void main(String[] args) {
        CategoryBean categoryBean = new CategoryBean(1, "Car Brand");
        System.out.println(categoryBean);
    }
}
