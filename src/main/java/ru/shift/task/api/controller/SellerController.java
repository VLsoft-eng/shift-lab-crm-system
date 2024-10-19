package ru.shift.task.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shift.task.api.Paths;
import ru.shift.task.api.dto.SellerContactPatchRequest;
import ru.shift.task.api.dto.SellerCreateRequest;
import ru.shift.task.api.dto.SellerNamePatchRequest;
import ru.shift.task.domain.service.SellerService;

@RestController
@RequestMapping(Paths.SELLERS)
public class SellerController {

    SellerService sellerService;

    @Autowired
    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSeller(@Valid @RequestBody SellerCreateRequest sellerCreateRequest) {
        sellerService.createSeller(sellerCreateRequest);
        return ResponseEntity.ok("Seller created");
    }

    @PatchMapping("/{id}/edit/name")
    public ResponseEntity<?> editSeller(@PathVariable @Min(value = 1) Long id,
                                        @Valid @RequestBody SellerNamePatchRequest sellerNamePatchRequest) {
        sellerService.updateSellerName(sellerNamePatchRequest, id);
        return ResponseEntity.ok("Seller name updated");
    }

    @PatchMapping("/{id}/edit/contact-info")
    public ResponseEntity<?> editSeller(@PathVariable @Min(value = 1) Long id,
                                        @Valid @RequestBody SellerContactPatchRequest sellerContactPatchRequest) {
        sellerService.updateSellerContactInfo(sellerContactPatchRequest, id);
        return ResponseEntity.ok("Seller name updated");
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteSeller(@PathVariable @Min(value = 1) Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.ok("Seller deleted");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSeller(@PathVariable @Valid @Min(value = 1) Long id) {
        return ResponseEntity.ok(sellerService.readSeller(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllSellers() {
        return ResponseEntity.ok(sellerService.readAllSellers());
    }
}
