package ZhienlinAzamat.RN_uchet.services;

import ZhienlinAzamat.RN_uchet.Entities.Inventory;
import ZhienlinAzamat.RN_uchet.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class InventoryService {
    private InventoryRepository inventoryRepository;

    @Autowired
    public void setInventoryRepository(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void saveOrUpdate(Inventory inventory){
        inventoryRepository.save(inventory);
    }

    public Inventory findById(Long id){
        return inventoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Can't found product with id = " + id));
    }

    public void delete(Long id) {
        inventoryRepository.deleteById(id);
    }

    public Page<Inventory> findByPage(int pageumber, int pageSize){
        return  inventoryRepository.findAll(PageRequest.of(pageumber,pageSize));
    }

    public List<Inventory> findAll(){
        return  inventoryRepository.findAll();
    }

    public Page<Inventory> findAll(Integer page){
        if (page < 1L) {
            page = 1;
        }
        return  inventoryRepository.findAll(PageRequest.of(page - 1, 4));
    }

    public Page<Inventory> findAll(Specification<Inventory> spec, Integer page) {
        if (page < 1L) {
            page = 1;
        }
        return inventoryRepository.findAll(spec, PageRequest.of(page - 1, 20));
    }
}
