package com.p5.adoptions.service;

import com.p5.adoptions.repository.cats.Cat;
import com.p5.adoptions.repository.dogs.Dog;
import com.p5.adoptions.repository.shelters.AnimalShelter;
import com.p5.adoptions.repository.shelters.AnimalShelterRepository;
import com.p5.adoptions.service.DTO.CatDTO;
import com.p5.adoptions.service.DTO.DogDTO;
import com.p5.adoptions.service.DTO.ListDTO;
import com.p5.adoptions.service.DTO.ShelterDTO;
import com.p5.adoptions.service.adapters.CatAdapter;
import com.p5.adoptions.service.adapters.DogAdapter;
import com.p5.adoptions.service.adapters.ShelterAdapter;
import com.p5.adoptions.service.exceptions.ApiError;
import com.p5.adoptions.service.exceptions.ShelterLocationException;
import com.p5.adoptions.service.exceptions.ValidationException;
import com.p5.adoptions.service.exceptions.Violation;
import com.p5.adoptions.service.validations.OnCreate;
import com.p5.adoptions.service.validations.OnUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class AnimalShelterService {

    private final AnimalShelterRepository animalShelterRepository;

    public AnimalShelterService(AnimalShelterRepository animalShelterRepository) {
        this.animalShelterRepository = animalShelterRepository;
    }


    public ListDTO<ShelterDTO> findAll() {
        ListDTO<ShelterDTO> listDTO = new ListDTO<>();

        List<AnimalShelter> allShelters = animalShelterRepository.findAll();

        listDTO.setData(ShelterAdapter.toDTOList(allShelters));
        listDTO.setTotalCount(animalShelterRepository.count());

        return listDTO;
    }

    @Validated(OnCreate.class)
    public ShelterDTO createShelter(@Valid ShelterDTO animalShelter) {
        validateShelterLocation(animalShelter);
        AnimalShelter shelter = ShelterAdapter.fromDTO(animalShelter);
        return ShelterAdapter.toDTO(animalShelterRepository.save(shelter));
    }

    private void validateShelterLocation(ShelterDTO animalShelter) {
        String location = animalShelter.getLocation().toLowerCase(Locale.ROOT);
        if(!location.contains("brasov") && !location.contains("iasi")) {
            throw new ShelterLocationException("must enter Brasov or Iasi");
        }
    }

    private void validateShelter(ShelterDTO shelterDTO) {
        ApiError error = new ApiError(HttpStatus.CONFLICT, "Shelter validation failed");

        if(shelterDTO.getDogs().isEmpty()) {
            error.getViolations().add(new Violation("dogs", "Minimum 1 dog"));
        }
        if(shelterDTO.getName().contains("_")) {
            error.getViolations().add(new Violation("name", "No underscore in name "));
        }

        if(!error.getViolations().isEmpty()) {
            throw new ValidationException(error);
        }
    }


    @Validated(OnUpdate.class)
    public ShelterDTO updateShelter(Integer id, @Valid ShelterDTO animalShelter) {
        validateShelterLocation(animalShelter);
        Optional<AnimalShelter> oldShelter = animalShelterRepository.findById(id);
        if(oldShelter.isPresent()) {
            animalShelter.setId(id);
            return ShelterAdapter.toDTO(animalShelterRepository
                    .save(ShelterAdapter.fromDTO(animalShelter)));
        }

        throw new EntityNotFoundException("Shelter with id " + id + " not found");

    }

    public ShelterDTO findById(Integer id) {
        AnimalShelter shelter = getShelterById(id);
        return ShelterAdapter.toDTO(shelter);
    }


    public void deleteShelter(Integer id) {
        animalShelterRepository.deleteById(id);
    }

    public List<CatDTO> findAllCatsByShelter(Integer shelterId) {
        AnimalShelter shelter = getShelterById(shelterId);
        return CatAdapter.toDTOList(shelter.getCats());
    }

    public List<CatDTO> addNewCatToShelter(Integer shelterId, CatDTO catDTO) {
        AnimalShelter shelter = getShelterById(shelterId);
        shelter.getCats().add(CatAdapter.fromDTO(catDTO));
        animalShelterRepository.save(shelter);
        return CatAdapter.toDTOList(shelter.getCats());
    }

    public CatDTO updateCatInShelter(Integer shelterId, Integer catId, CatDTO catDTO) {
        AnimalShelter shelter = getShelterById(shelterId);
        Cat cat = CatAdapter.fromDTO(catDTO);
        List<Cat> newCats = shelter.getCats().stream().map(c -> {
            if (c.getId().equals(catId)) {
                cat.setId(catId);
                return cat;
            }
            return c;
        }).collect(Collectors.toList());
        shelter.setCats(newCats);
        animalShelterRepository.save(shelter);
        return CatAdapter.toDTO(cat);
    }

    public void deleteCatInShelter(Integer shelterId, Integer catId) {
        AnimalShelter shelter = getShelterById(shelterId);
        List<Cat> newCats = shelter.getCats().stream().filter(c -> !c.getId().equals(catId)).collect(Collectors.toList());
        shelter.setCats(newCats);
        animalShelterRepository.save(shelter);
    }

    public List<DogDTO> findAllDogsByShelter(Integer shelterId) {
        AnimalShelter shelter = getShelterById(shelterId);
        return DogAdapter.toDTOList(shelter.getDogs());
    }

    private AnimalShelter getShelterById(Integer id) {
        Optional<AnimalShelter> optional = animalShelterRepository.findById(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("Shelter with id " + id + " not found"));
    }

    public List<DogDTO> addNewDogToShelter(Integer shelterId, DogDTO dogDTO) {
        AnimalShelter shelter = getShelterById(shelterId);
        Dog dog = DogAdapter.fromDTO(dogDTO);
        shelter.getDogs().add(dog);

        animalShelterRepository.save(shelter);

        return DogAdapter.toDTOList(shelter.getDogs());
    }

    public DogDTO updateDogInShelter(Integer shelterId, Integer dogId, DogDTO dogDTO) {
        AnimalShelter shelter = getShelterById(shelterId);
        Dog dog = DogAdapter.fromDTO(dogDTO);
        List<Dog> newDogs = shelter.getDogs().stream().map(c -> {
            if (c.getId().equals(dogId)) {
                dog.setId(dogId);
                return dog;
            }
            return c;
        }).collect(Collectors.toList());
        shelter.setDogs(newDogs);
        animalShelterRepository.save(shelter);
        return DogAdapter.toDTO(dog);
    }

    public void deleteDogInShelter(Integer shelterId, Integer dogId) {
        AnimalShelter shelter = getShelterById(shelterId);

        boolean removed = shelter.getDogs().removeIf(d -> d.getId().equals(dogId));

        animalShelterRepository.save(shelter);

        if(!removed) {
            throw new RuntimeException("Already deleted or entity missing");
        }
    }
}
