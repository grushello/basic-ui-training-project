package lv.bootcamp.shelter.repository;

import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory data store for shelter animals.
 * Acts as the data access layer — AnimalService delegates to this class.
 * No database is used; all data resets on each application restart.
 * Pre-seeded with six animals on startup.
 */
@Repository
public class AnimalRepository {

    private final Map<Long, Animal> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    public AnimalRepository() {
        seed("Luna",   AnimalType.CAT,   "Bengal", 2,
                "Calm and affectionate. Loves cuddles.",
                "Luna.jpeg",
                AnimalStatus.AVAILABLE);
        seed("Diora",    AnimalType.DOG,   "Cane Corso",          4,
                "Energetic and friendly. Great with kids.",
                "Diora.jpeg",
                AnimalStatus.AVAILABLE);
        seed("Brita",  AnimalType.DOG,   "German Sheppard",            1,
                "Playful and intelligent. Already trained.",
                "Brita.png",
                AnimalStatus.ADOPTED);
        seed("Dorian", AnimalType.CAT,   "Grey Longhair",      5,
                "Independent but friendly. Indoor only.",
                "Dorians.jpeg",
                AnimalStatus.AVAILABLE);
        seed("Dorian", AnimalType.CAT,   "Tortoiseshell  Shorthair",      4,
                "Curious and active. Needs a yard.",
                "Murka.jpg",
                AnimalStatus.AVAILABLE);
        seed("Pepper", AnimalType.OTHER, "Rabbit",            2,
                "Gentle and curious. Loves fresh vegetables.",
                "https://www.ohiohollandlops.com/uploads/5/0/2/8/50280945/2489525_orig.jpg",
                AnimalStatus.AVAILABLE);
    }

    /**
     * Returns an atomically incremented ID for use when creating a new animal.
     */
    public long nextId() {
        return idSequence.incrementAndGet();
    }

    /**
     * Returns all animals sorted by ID.
     */
    public List<Animal> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparingLong(Animal::getId))
                .toList();
    }

    /**
     * Returns a single animal by ID, or empty if not found.
     */
    public Optional<Animal> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Persists an animal. The animal must already have an ID assigned via nextId().
     */
    public Animal save(Animal animal) {
        store.put(animal.getId(), animal);
        return animal;
    }

    private void seed(String name, AnimalType type, String breed, int age,
                      String description, String imageUrl, AnimalStatus status) {
        long id = idSequence.incrementAndGet();
        store.put(id, new Animal(id, name, type, breed, age, description, status, imageUrl));
    }
}
