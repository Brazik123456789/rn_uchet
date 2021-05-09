package ZhienlinAzamat.RN_uchet.Entities;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "action_table")
@Data
@NoArgsConstructor
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
    @Column(name = "action")
    private String operation;
    @Column
    private double count;
    @Column(name = "giving_user")
    private String givingUser;
    @Column
    private String recipient;
    @Column(name = "giving_user_ostatok")
    private double givingUserOstatok;
    @Column
    private Date date;
    @Column
    private String comment;

    public Action(Inventory inventory, String operation, double count, String givingUser, String recipient, double givingUserOstatok, Date date, String comment) {
        this.inventory = inventory;
        this.operation = operation;
        this.count = count;
        this.givingUser = givingUser;
        this.recipient = recipient;
        this.givingUserOstatok = givingUserOstatok;
        this.date = date;
        this.comment = comment;
    }
}
