package ZhienlinAzamat.RN_uchet.utils;

import ZhienlinAzamat.RN_uchet.Entities.Inventory;
import ZhienlinAzamat.RN_uchet.repositories.specifications.InventorySpecifications;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
@Getter
public class InventoryFilter {
    private Specification<Inventory> spec;
    private StringBuilder filterDefinition;

    public InventoryFilter(Map<String, String> map) {
        this.spec = Specification.where(null);
        this.filterDefinition = new StringBuilder();

        if (map.containsKey("inventory_name") && !map.get("inventory_name").isEmpty()) {
            String inventory_name = map.get("inventory_name");
            spec = spec.and(InventorySpecifications.inventory_nameLike(inventory_name));
            filterDefinition.append("&inventory_name=").append(inventory_name);
        }

        if (map.containsKey("inventory_number") && !map.get("inventory_number").isEmpty()) {
            String inventory_number = map.get("inventory_number");
            spec = spec.and(InventorySpecifications.inventory_numberLike(inventory_number));
            filterDefinition.append("&inventory_number=").append(inventory_number);
        }

        if (map.containsKey("addedUser") && !map.get("addedUser").isEmpty()) {
            spec = spec.and(InventorySpecifications.inventory_addedUser(map.get("addedUser")));
        }
    }
}
