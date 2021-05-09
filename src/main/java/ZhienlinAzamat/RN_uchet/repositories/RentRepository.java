package ZhienlinAzamat.RN_uchet.repositories;

import ZhienlinAzamat.RN_uchet.Entities.Action;
import ZhienlinAzamat.RN_uchet.Entities.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long>, JpaSpecificationExecutor<Rent> {

    @Query(value = "SELECT * FROM rent_table WHERE inventory_id = ?1", nativeQuery = true)
    List<Rent> findByInventoryId(Long id);
}
