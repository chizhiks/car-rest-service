package ua.foxminded.chyzhov.carrestservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.chyzhov.carrestservice.dto.MakeDto;
import ua.foxminded.chyzhov.carrestservice.service.MakeService;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/makes")
@RequiredArgsConstructor
public class MakeController {

    private final MakeService makeService;

    @GetMapping
    public ResponseEntity<Page<MakeDto>> getAllMakes(Pageable pageable) {

        Page<MakeDto> makes = makeService.findAll(pageable);

        return ResponseEntity.ok(makes);
    }

    @PostMapping
    public ResponseEntity<MakeDto> createMake(@Valid @RequestBody MakeDto makeDto) {

        MakeDto savedMake = makeService.save(makeDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMake);
    }

    @GetMapping("/{makeId}")
    public ResponseEntity<MakeDto> getMakeById(@PathVariable Integer makeId) {

        MakeDto make = makeService.findById(makeId);

        return ResponseEntity.ok(make);
    }

    @GetMapping("/name/{makeName}")
    public ResponseEntity<MakeDto> getMakeByName(@PathVariable String makeName) {

        MakeDto make = makeService.findByName(makeName);

        return ResponseEntity.ok(make);
    }

    @PutMapping("/{makeId}")
    public ResponseEntity<MakeDto> updateMake(@PathVariable Integer makeId, @Valid @RequestBody MakeDto makeDto) {

        MakeDto updatedMake = makeService.update(makeId, makeDto);

        return ResponseEntity.ok(updatedMake);
    }

    @DeleteMapping("/{makeId}")
    public ResponseEntity<Void> deleteMake(@PathVariable Integer makeId) {

        makeService.deleteById(makeId);

        return ResponseEntity.noContent().build();
    }

}
