package ZhienlinAzamat.RN_uchet.services;

import ZhienlinAzamat.RN_uchet.Entities.Action;
import ZhienlinAzamat.RN_uchet.repositories.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Month;
import java.time.Year;
import java.util.List;

@Service
public class ActionService {
    private ActionRepository actionRepository;

    @Autowired
    public void setActionRepository(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public void saveOrUpdate(Action action){
        actionRepository.save(action);
    }

    public List<Action> findByInventoryId(Long inventoryId){
        return actionRepository.findByInventoryId(inventoryId);
    }
    public Action findById(Long id){
        return actionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Can't found product with id = " + id));
    }
    public List<Integer> findYears(){
        return actionRepository.findYears();
    }
    public List<Integer> findMonths(){
        return actionRepository.findMonths();
    }

    public List<Action> findByYearAndMonth(Long year, Long month, String user){
        return actionRepository.findByYearAndMonth(year, month, user);
    }

}