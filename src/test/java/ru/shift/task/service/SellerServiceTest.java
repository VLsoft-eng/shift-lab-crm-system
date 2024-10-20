package ru.shift.task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.shift.task.api.dto.SellerContactPatchRequest;
import ru.shift.task.api.dto.SellerCreateRequest;
import ru.shift.task.api.dto.SellerDto;
import ru.shift.task.api.dto.SellerNamePatchRequest;
import ru.shift.task.data.entity.Seller;
import ru.shift.task.data.repository.SellerRepository;
import ru.shift.task.domain.exception.SellerNotFoundException;
import ru.shift.task.domain.mapper.SellerMapper;
import ru.shift.task.domain.service.SellerService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private SellerMapper sellerMapper;

    @InjectMocks
    private SellerService sellerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create Seller")
    void createSellerTest() {
        SellerCreateRequest request = new SellerCreateRequest("New Seller", "Contact Info");
        Seller sellerEntity = new Seller();
        when(sellerMapper.toEntity(any(SellerCreateRequest.class))).thenReturn(sellerEntity);

        sellerService.createSeller(request);

        verify(sellerRepository, times(1)).save(sellerEntity);
    }

    @Test
    @DisplayName("Read Seller")
    void readSellerTest() {
        Seller sellerEntity = new Seller();
        sellerEntity.setId(1L);

        SellerDto sellerDto = SellerDto.builder()
                .id(1L)
                .name("Seller Name")
                .contactInfo("Contact Info")
                .build();

        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(sellerEntity));
        when(sellerMapper.toDto(any(Seller.class))).thenReturn(sellerDto);

        SellerDto result = sellerService.readSeller(1L);

        assertEquals(sellerDto, result);
        verify(sellerRepository, times(1)).findById(1L);
        verify(sellerMapper, times(1)).toDto(sellerEntity);
    }

    @Test
    @DisplayName("Read Seller throw exception")
    void readSellerThrowExceptionTest() {
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(SellerNotFoundException.class, () -> sellerService.readSeller(1L));
        verify(sellerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Update Seller name")
    void updateSellerNameTest() {
        SellerNamePatchRequest request = new SellerNamePatchRequest("Updated Name");
        Seller existingSeller = new Seller();
        existingSeller.setId(1L);
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(existingSeller));

        sellerService.updateSellerName(request, 1L);

        assertEquals("Updated Name", existingSeller.getName());
        verify(sellerRepository, times(1)).save(existingSeller);
    }

    @Test
    @DisplayName("Update Seller contact information")
    void updateSellerContactInfoTest() {
        SellerContactPatchRequest request = new SellerContactPatchRequest("Updated Contact Info");
        Seller existingSeller = new Seller();
        existingSeller.setId(1L);
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(existingSeller));

        sellerService.updateSellerContactInfo(request, 1L);

        assertEquals("Updated Contact Info", existingSeller.getContactInfo());
        verify(sellerRepository, times(1)).save(existingSeller);
    }

    @Test
    @DisplayName("Delete Seller")
    void deleteSellerTest() {
        when(sellerRepository.existsById(anyLong())).thenReturn(true);

        sellerService.deleteSeller(1L);

        verify(sellerRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete seller throw exception")
    void deleteSellerThrowExceptionTest() {
        when(sellerRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(SellerNotFoundException.class, () -> sellerService.deleteSeller(1L));
        verify(sellerRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Read all sellers")
    void readAllSellersTest() {
        List<Seller> sellers = List.of(new Seller(), new Seller());

        List<SellerDto> sellerDtos = List.of(
                SellerDto.builder().id(1L).name("Seller 1").contactInfo("Contact 1").build(),
                SellerDto.builder().id(2L).name("Seller 2").contactInfo("Contact 2").build()
        );

        when(sellerRepository.findAll()).thenReturn(sellers);
        when(sellerMapper.toDto(sellers)).thenReturn(sellerDtos);

        List<SellerDto> result = sellerService.readAllSellers();

        assertEquals(2, result.size());
        assertEquals(sellerDtos, result);
        verify(sellerRepository, times(1)).findAll();
        verify(sellerMapper, times(1)).toDto(sellers);
    }
}
