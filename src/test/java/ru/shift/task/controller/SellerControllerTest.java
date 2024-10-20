package ru.shift.task.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.shift.task.api.controller.SellerController;
import ru.shift.task.api.dto.SellerContactPatchRequest;
import ru.shift.task.api.dto.SellerCreateRequest;
import ru.shift.task.api.dto.SellerDto;
import ru.shift.task.api.dto.SellerNamePatchRequest;
import ru.shift.task.domain.service.SellerService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SellerControllerTest {

    @InjectMocks
    private SellerController sellerController;

    @Mock
    private SellerService sellerService;

    private SellerCreateRequest sellerCreateRequest;
    private SellerNamePatchRequest sellerNamePatchRequest;
    private SellerContactPatchRequest sellerContactPatchRequest;
    private Long sellerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sellerId = 1L;

        sellerCreateRequest = SellerCreateRequest.builder()
                .name("John Doe")
                .contactInfo("john.doe@example.com")
                .build();

        sellerNamePatchRequest = new SellerNamePatchRequest("Jane Doe");

        sellerContactPatchRequest = new SellerContactPatchRequest("jane.doe@example.com");
    }

    @Test
    @DisplayName("Create seller")
    void createSellerTest() {
        ResponseEntity<?> response = sellerController.createSeller(sellerCreateRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Seller created");

        verify(sellerService, times(1)).createSeller(sellerCreateRequest);
    }

    @Test
    @DisplayName("Edit seller name")
    void editSellerNameTest() {
        ResponseEntity<?> response = sellerController.editSeller(sellerId, sellerNamePatchRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Seller name updated");

        verify(sellerService, times(1)).updateSellerName(sellerNamePatchRequest, sellerId);
    }

    @Test
    @DisplayName("Edit seller contact")
    void editSellerContactInfoTest() {
        ResponseEntity<?> response = sellerController.editSeller(sellerId, sellerContactPatchRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Seller name updated");

        verify(sellerService, times(1)).updateSellerContactInfo(sellerContactPatchRequest, sellerId);
    }

    @Test
    @DisplayName("Delete Seller")
    void deleteSellerTest() {
        ResponseEntity<?> response = sellerController.deleteSeller(sellerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Seller deleted");

        verify(sellerService, times(1)).deleteSeller(sellerId);
    }

    @Test
    @DisplayName("Get seller")
    void getSellerTest() {
        SellerDto sellerDto = new SellerDto(sellerId, "John Doe", "john.doe@example.com", LocalDateTime.now());
        when(sellerService.readSeller(sellerId)).thenReturn(sellerDto);

        ResponseEntity<?> response = sellerController.getSeller(sellerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sellerDto);

        verify(sellerService, times(1)).readSeller(sellerId);
    }

    @Test
    @DisplayName("Get all sellers")
    void getAllSellersTest() {
        SellerDto sellerDto = new SellerDto(sellerId, "John Doe", "john.doe@example.com", LocalDateTime.now());
        when(sellerService.readAllSellers()).thenReturn(List.of(sellerDto));

        ResponseEntity<?> response = sellerController.getAllSellers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(List.of(sellerDto));

        verify(sellerService, times(1)).readAllSellers();
    }
}

