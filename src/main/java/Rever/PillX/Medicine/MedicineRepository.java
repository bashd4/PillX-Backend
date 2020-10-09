package Rever.PillX.Medicine;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicineRepository extends MongoRepository<Medicine, String> {
    Medicine findByidentifier(String identifier);
}
