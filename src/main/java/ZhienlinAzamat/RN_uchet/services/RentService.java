package ZhienlinAzamat.RN_uchet.services;

import ZhienlinAzamat.RN_uchet.Entities.Action;
import ZhienlinAzamat.RN_uchet.Entities.Inventory;
import ZhienlinAzamat.RN_uchet.Entities.Rent;
import ZhienlinAzamat.RN_uchet.repositories.InventoryRepository;
import ZhienlinAzamat.RN_uchet.repositories.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class RentService {
    private RentRepository rentRepository;

    @Autowired
    public void setInventoryRepository(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }

    public void saveOrUpdate(Rent rent){
        rentRepository.save(rent);
    }

    public Rent findById(Long id){
        return rentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Can't found product with id = " + id));
    }

    public void delete(Long id) {
        rentRepository.deleteById(id);
    }
    public List<Rent> findAll(){
        return  rentRepository.findAll();
    }
    public List<Rent> findByInventoryId(Long inventoryId){
        return rentRepository.findByInventoryId(inventoryId);
    }
}
