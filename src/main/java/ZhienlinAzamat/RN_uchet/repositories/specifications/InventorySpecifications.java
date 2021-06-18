package ZhienlinAzamat.RN_uchet.repositories.specifications;

import ZhienlinAzamat.RN_uchet.Entities.Inventory;
import org.springframework.data.jpa.domain.Specification;

public class InventorySpecifications {
    public static Specification<Inventory> inventory_nameLike(String inventory_name){
        return (Specification<Inventory>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), String.format("%%%s%%", inventory_name));
    }

    public static Specification<Inventory> inventory_numberLike(String inventory_number){
        return (Specification<Inventory>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("inventory_number"), String.format("%%%s%%", inventory_number));
    }

    public static Specification<Inventory> inventory_addedUser(String user){
        return (Specification<Inventory>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("addedUser"), String.format("%%%s%%", user));
    }
}
