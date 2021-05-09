package ZhienlinAzamat.RN_uchet.Entities;

import com.sun.javafx.geom.transform.Identity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "rent_table")
@Data
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
    @Column
    private String recipient;
    @Column
    private double count;

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", inventory=" + inventory +
                ", recipient='" + recipient + '\'' +
                ", count=" + count +
                '}';
    }
}
