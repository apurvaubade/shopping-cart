package com.example.demo.entity;



import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "section_id", nullable = false)
//    private Section section;
    @ManyToOne(fetch = FetchType.LAZY) // Adjust fetch type as needed
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private Section section;

    // Getters and Setters
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

    public Section getSection() {
        System.out.println("section"+ section);
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
