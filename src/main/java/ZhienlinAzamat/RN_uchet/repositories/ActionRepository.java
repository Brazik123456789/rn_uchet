package ZhienlinAzamat.RN_uchet.repositories;

import ZhienlinAzamat.RN_uchet.Entities.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    @Query(value = "SELECT * FROM action_table WHERE inventory_id = ?1 ORDER BY date DESC", nativeQuery = true)
    List<Action> findByInventoryId(Long id);

    @Query(value = "SELECT distinct year(date) FROM action_table", nativeQuery = true)
    List<Integer> findYears();

    @Query(value = "SELECT distinct month(date) FROM action_table", nativeQuery = true)
    List<Integer> findMonths();

    @Query(value = "SELECT * FROM action_table where year(date) = ?1 and month(date) = ?2", nativeQuery = true)
    List<Action> findByYearAndMonth(Long year, Long month);

}