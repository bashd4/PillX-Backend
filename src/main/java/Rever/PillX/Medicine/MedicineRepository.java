package Rever.PillX.Medicine;

import org.springframework.data.mongodb.repository.MongoRepository;

/*
 * Methods to search the medicine repository
 */
public interface MedicineRepository extends MongoRepository<Medicine, String> {
    Medicine findByidentifier(String identifier);
    Medicine findBybarcode(String barcode);
    Medicine findByName(String name);
}
