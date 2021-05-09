package ZhienlinAzamat.RN_uchet.Entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "inventory_table")
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String inventory_number;
    @Column
    private String name;
    @Column
    private String measure;
    @Column(name = "availability")
    private double ostatok;
    @Column(name = "added_user")
    private String addedUser;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.REMOVE)
    private List<Action> actions;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.REMOVE)
    private List<Rent> rents;
}
