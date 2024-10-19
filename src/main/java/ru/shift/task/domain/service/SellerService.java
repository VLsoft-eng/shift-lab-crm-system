package ru.shift.task.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shift.task.api.dto.SellerCreateRequest;
import ru.shift.task.api.dto.SellerDto;
import ru.shift.task.api.dto.SellerPatchRequest;
import ru.shift.task.data.entity.Seller;
import ru.shift.task.data.repository.SellerRepository;
import ru.shift.task.domain.exception.SellerNotFoundException;
import ru.shift.task.domain.mapper.SellerMapper;

import java.util.List;

@Service
public class SellerService {

    private SellerRepository sellerRepository;
    private SellerMapper sellerMapper;

    @Autowired
    private void setSellerRepository(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Autowired
    private void setSellerMapper(SellerMapper sellerMapper) {
        this.sellerMapper = sellerMapper;
    }

    public void createSeller(SellerCreateRequest seller) {
        sellerRepository.save(sellerMapper.toEntity(seller));
    }

    public SellerDto readSeller(Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new SellerNotFoundException(id));

        return sellerMapper.toDto(seller);
    }

    public void updateSeller(SellerPatchRequest seller, Long id) {
        Seller existSeller = sellerRepository.findById(id)
                .orElseThrow(() -> new SellerNotFoundException(id));

        if (!seller.name().isEmpty()) {
            existSeller.setName(seller.name());
        }

        if (!seller.contactInfo().isEmpty()) {
            existSeller.setContactInfo(seller.contactInfo());
        }

        sellerRepository.save(existSeller);
    }

    public void deleteSeller(Long id) {
        boolean isSellerExist = sellerRepository.existsById(id);

        if (isSellerExist) {
            sellerRepository.deleteById(id);
        } else {
            throw new SellerNotFoundException(id);
        }
    }

    public List<SellerDto> readAllSellers() {
        List<Seller> sellers = sellerRepository.findAll();

        return sellerMapper.toDto(sellers);
    }
}
