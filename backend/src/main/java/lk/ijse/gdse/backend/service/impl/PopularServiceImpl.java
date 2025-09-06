package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.PopularDto;
import lk.ijse.gdse.backend.dto.ProductDto;
import lk.ijse.gdse.backend.entity.Popular;
import lk.ijse.gdse.backend.entity.Product;
import lk.ijse.gdse.backend.repository.PopularRepository;
import lk.ijse.gdse.backend.service.PopularService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PopularServiceImpl implements PopularService {

    private final PopularRepository popularRepository;
    private final ModelMapper modelMapper;

    @Override
    public void savePopular(PopularDto popularDto) {
        Popular popular;

        if (popularDto.getPopularId() != null) {
            popular = popularRepository.findById(popularDto.getPopularId())
                    .orElse(new Popular());
        } else {
            popular = new Popular();
        }

        popular.setName(popularDto.getName());
        popular.setCategory(popularDto.getCategory());
        popular.setDescription(popularDto.getDescription());
        popular.setPrice(popularDto.getPrice());
        popular.setImageUrl(popularDto.getImageUrl());

        popularRepository.save(popular);
    }

    @Override
    public List<PopularDto> getAllPopularIte() {
        List<Popular>populars = popularRepository.findAll();
        List<PopularDto> popularDtos = new ArrayList<>();
        for (Popular popular : populars) {
            popularDtos.add(modelMapper.map(popular, PopularDto.class));
        }
        return popularDtos;
    }

    @Override
    public void updatePopular(PopularDto popularDto) {
        if (popularDto.getPopularId() == null) {
            savePopular(popularDto);
            return;
        }

        Popular popular = popularRepository.findById(popularDto.getPopularId())
                .orElseThrow(() -> new RuntimeException("Popular Item not found with ID: " + popularDto.getPopularId()));

        popular.setName(popularDto.getName());
        popular.setCategory(popularDto.getCategory());
        popular.setDescription(popularDto.getDescription());
        popular.setPrice(popularDto.getPrice());
        popular.setImageUrl(popularDto.getImageUrl());

        popularRepository.save(popular);
    }

    @Override
    public void deletePopular(Long id) {
        Popular popular = popularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Popular Item not found with id: " + id));
        popularRepository.delete(popular);
    }


}
