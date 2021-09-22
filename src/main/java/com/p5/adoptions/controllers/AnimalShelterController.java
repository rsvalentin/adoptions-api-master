package com.p5.adoptions.controllers;


import com.p5.adoptions.service.AnimalShelterService;
import com.p5.adoptions.service.DTO.CatDTO;
import com.p5.adoptions.service.DTO.DogDTO;
import com.p5.adoptions.service.DTO.ListDTO;
import com.p5.adoptions.service.DTO.ShelterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shelters")
public class AnimalShelterController {

    AnimalShelterService animalShelterService;

    public AnimalShelterController(AnimalShelterService animalShelterService) {
        this.animalShelterService = animalShelterService;
    }

    @GetMapping()
    public ResponseEntity<ListDTO<ShelterDTO>> getShelters() {
        return ResponseEntity.ok(animalShelterService.findAll());
    }

    // dto pt shelter

    @GetMapping("/{id}")
    public ResponseEntity<ShelterDTO> getShelter(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(animalShelterService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<ShelterDTO> createShelter(@Valid @RequestBody ShelterDTO animalShelter) {
        return ResponseEntity.ok(animalShelterService.createShelter(animalShelter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShelterDTO> updateShelter(@PathVariable("id") Integer id, @Valid @RequestBody ShelterDTO animalShelter) {
        return ResponseEntity.ok(animalShelterService.updateShelter(id, animalShelter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteShelter(@PathVariable("id") Integer id) {
        animalShelterService.deleteShelter(id);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    // dto pt cat

    @GetMapping("/{shelderId}/cats}")
    public ResponseEntity<List<CatDTO>> getCatsForShelter(@PathVariable("shelderId") Integer shelterId) {
        return ResponseEntity.ok(animalShelterService.findAllCatsByShelter(shelterId));
    }

    @PutMapping("{shelterId/cats}")
    public ResponseEntity<List<CatDTO>> addNewCatToShelter(@PathVariable("shelterId") Integer shelterId, @RequestBody CatDTO catDTO) {
        return ResponseEntity.ok(animalShelterService.addNewCatToShelter(shelterId, catDTO));
    }

    @PatchMapping("/{shelterId}/cats/{catId}")
    public ResponseEntity<CatDTO> updateCatInShelter(@PathVariable("shelterId") Integer shelterId, @PathVariable("catId") Integer catId, @RequestBody CatDTO catDTO) {
        return ResponseEntity.ok(animalShelterService.updateCatInShelter(shelterId, catId, catDTO));
    }

    @DeleteMapping("/{shelterId}/cats/{catId}")
    public ResponseEntity<Object> deleteCatInShelter(@PathVariable("shelterId") Integer shelterId, @PathVariable("catId") Integer catId) {
        animalShelterService.deleteCatInShelter(shelterId, catId);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    // dto pt dog

    @GetMapping("/{shelterId}/dogs")
    public ResponseEntity<List<DogDTO>> getDogsForShelter(@PathVariable("shelterId") Integer shelterId) {
        return ResponseEntity.ok(animalShelterService.findAllDogsByShelter(shelterId));
    }

    @PutMapping("/{shelterId}/dogs")
    public ResponseEntity<List<DogDTO>> addNewDogToShelter(@PathVariable("shelterId") Integer shelterId, @RequestBody DogDTO dogDTO) {
        return ResponseEntity.ok(animalShelterService.addNewDogToShelter(shelterId, dogDTO));
    }

    @PatchMapping("/{shelterId}/dogs/{dogId}")
    public ResponseEntity<DogDTO> updateDogInShelter(@PathVariable("shelterId") Integer shelterId, @PathVariable("dogId") Integer dogId, @RequestBody DogDTO dogDTO) {
        return ResponseEntity.ok(animalShelterService.updateDogInShelter(shelterId, dogId, dogDTO));
    }

    @DeleteMapping("/{shelterId}/dogs/{dogId}")
    public ResponseEntity<Object> deleteDogInShelter(@PathVariable("shelterId") Integer shelterId, @PathVariable("dogId") Integer dogId) {
        animalShelterService.deleteDogInShelter(shelterId, dogId);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }
}
